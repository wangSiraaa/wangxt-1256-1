<template>
  <div>
    <n-button
      style="margin-bottom: 16px;"
      @click="$router.back()"
      quaternary
    >
      ← 返回限制列表
    </n-button>

    <n-card title="申诉提交" :bordered="false" style="max-width: 860px; margin: 0 auto;">
      <n-spin :show="loadingRestriction">
        <template v-if="restriction">
          <n-alert type="warning" :show-icon="true" style="margin-bottom: 20px;">
            <template #title>您正在申诉的风控限制</template>
            <n-space vertical :size="4">
              <div>单号：<strong>{{ restriction.restrictionNo }}</strong></div>
              <div>类型：{{ restriction.restrictionTypeDesc }}</div>
              <div>原因：{{ restriction.triggerReason }}</div>
            </n-space>
          </n-alert>
        </template>
      </n-spin>

      <n-form ref="formRef" :model="form" :rules="rules" label-placement="top" label-width="120px">
        <n-grid :cols="2" :x-gap="16">
          <n-form-item label="休息开始时间" path="restStartTime" :span="1">
            <n-date-picker
              v-model:value="form.restStartTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%;"
            />
          </n-form-item>
          <n-form-item label="休息结束时间" path="restEndTime" :span="1">
            <n-date-picker
              v-model:value="form.restEndTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%;"
              @update:value="calcRestMinutes"
            />
          </n-form-item>
          <n-form-item label="实际休息时长（分钟）" path="restMinutes" :span="1">
            <n-input-number
              v-model:value="form.restMinutes"
              :min="1"
              :max="720"
              placeholder="休息时长"
              style="width: 100%;"
            />
            <n-text depth="3" style="font-size: 12px;">
              需 ≥ 阈值 {{ threshold.requireRestMinutes }} 分钟才能有效解除限制
            </n-text>
          </n-form-item>
          <n-form-item label="休息证明文件" path="restProofUrl" :span="1">
            <n-upload
              :show-file-list="true"
              :max="3"
              :custom-request="handleUpload"
              :on-remove="handleRemove"
              accept="image/*,.pdf,.doc,.docx"
            >
              <n-button>上传休息证明（图片/PDF等）</n-button>
              <template #tip>
                支持上传多张图片或PDF，建议包含打卡记录、休息场所照片等
              </template>
            </n-upload>
            <n-input v-if="form.restProofUrl" v-model:value="form.restProofUrl" style="margin-top: 8px; display: none;" />
          </n-form-item>
          <n-form-item label="休息证明说明" path="restProofDesc" :span="2">
            <n-input
              v-model:value="form.restProofDesc"
              type="textarea"
              :rows="2"
              placeholder="请说明休息证明的内容来源、可验证方式等"
            />
          </n-form-item>
          <n-form-item label="申诉理由（必填）" path="appealReason" :span="2">
            <n-input
              v-model:value="form.appealReason"
              type="textarea"
              :rows="4"
              placeholder="请详细描述您实际休息的情况、为何系统判定有误、以及相关事实依据..."
            />
            <n-text depth="3" style="font-size: 12px;">
              完整填写申诉理由可大幅提高审核通过率
            </n-text>
          </n-form-item>
        </n-grid>

        <n-divider>材料自检清单</n-divider>
        <n-space vertical>
          <n-checkbox v-model:checked="checklist[0]" disabled>
            已填写休息时段（开始/结束时间）
          </n-checkbox>
          <n-checkbox v-model:checked="checklist[1]" disabled>
            已填写实际休息时长
          </n-checkbox>
          <n-checkbox v-model:checked="checklist[2]" disabled>
            已上传休息证明文件
          </n-checkbox>
          <n-checkbox v-model:checked="checklist[3]" disabled>
            已填写申诉理由
          </n-checkbox>
          <n-alert v-if="!materialComplete" type="error" :show-icon="true">
            ⚠️ 材料不完整的申诉将无法进入安全复核流程，请补充完整后再提交。
          </n-alert>
          <n-alert v-else type="success" :show-icon="true">
            ✅ 材料完整性检查通过，提交后将等待安全专员复核。
          </n-alert>
        </n-space>

        <div style="text-align: right; margin-top: 24px;">
          <n-button @click="$router.back()" style="margin-right: 12px;">取消</n-button>
          <n-button type="primary" :loading="submitting" :disabled="!materialComplete" @click="doSubmit">
            提交申诉
          </n-button>
        </div>
      </n-form>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { restrictionDetail, type RiskRestrictionVO } from '@/api/risk'
import { submitAppeal, type AppealSubmitDTO } from '@/api/appeal'
import { useMessage, type FormInst, type FormRules, type UploadCustomRequestOptions } from 'naive-ui'
import dayjs from 'dayjs'
import { uploadFile } from '@/api/common'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const appStore = useAppStore()
const { threshold, currentDriver } = storeToRefs(appStore)

const restrictionId = Number(route.params.restrictionId)
const formRef = ref<FormInst>()
const restriction = ref<RiskRestrictionVO | null>(null)
const loadingRestriction = ref(false)
const submitting = ref(false)
const uploadedUrls = ref<string[]>([])

const form = ref<AppealSubmitDTO>({
  restrictionId,
  driverId: currentDriver.value?.id || 1,
  appealReason: '',
  restProofUrl: '',
  restProofDesc: '',
  restStartTime: '',
  restEndTime: '',
  restMinutes: undefined
})

const checklist = computed(() => [
  !!form.value.restStartTime && !!form.value.restEndTime,
  form.value.restMinutes !== undefined && form.value.restMinutes > 0,
  !!form.value.restProofUrl || uploadedUrls.value.length > 0,
  form.value.appealReason.trim().length > 0
])

const materialComplete = computed(() => checklist.value.every(Boolean))

const rules: FormRules = {
  appealReason: { required: true, message: '请填写申诉理由', trigger: 'blur' },
  restStartTime: { required: true, message: '请选择休息开始时间', trigger: 'change' },
  restEndTime: { required: true, message: '请选择休息结束时间', trigger: 'change' },
  restMinutes: [
    { required: true, message: '请填写休息时长', trigger: 'blur' },
    {
      validator: (_r, value) => {
        if (value && value < 1) return new Error('休息时长不能为0')
        return true
      },
      trigger: 'blur'
    }
  ]
}

function calcRestMinutes() {
  if (form.value.restStartTime && form.value.restEndTime) {
    const m = dayjs(form.value.restEndTime).diff(dayjs(form.value.restStartTime), 'minute')
    if (m > 0) form.value.restMinutes = m
  }
}

watch([
  () => form.value.restStartTime,
  () => form.value.restEndTime
], calcRestMinutes)

async function handleUpload({ file, onFinish, onError }: UploadCustomRequestOptions) {
  try {
    const res = await uploadFile(file.file as File)
    if (res.url) {
      uploadedUrls.value.push(res.url)
      form.value.restProofUrl = uploadedUrls.value.join(',')
      onFinish()
      message.success('上传成功')
    } else {
      onError()
    }
  } catch (e) {
    onError()
    message.warning('上传失败，请重试')
  }
}

function handleRemove() {
  if (uploadedUrls.value.length > 0) {
    uploadedUrls.value.pop()
    form.value.restProofUrl = uploadedUrls.value.join(',')
  }
}

async function doSubmit() {
  try {
    await formRef.value?.validate()
  } catch (e) {
    return
  }
  submitting.value = true
  try {
    await submitAppeal(form.value)
    message.success('申诉提交成功，等待安全专员审核')
    setTimeout(() => {
      router.push('/driver/my-appeals')
    }, 1200)
  } catch (e: any) {
    message.error(e.message || '提交失败')
  } finally {
    submitting.value = false
  }
}

async function loadRestriction() {
  loadingRestriction.value = true
  try {
    restriction.value = await restrictionDetail(restrictionId)
  } catch (e) {
    message.warning('加载限制详情失败')
  } finally {
    loadingRestriction.value = false
  }
}

onMounted(loadRestriction)
</script>
