<template>
  <div>
    <div class="card-wrapper">
      <div class="section-title">我的申诉记录</div>
      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="true"
        :striped="true"
        :row-props="(row) => ({ style: { cursor: 'pointer' } })"
        size="small"
        @update:checked-row-keys="onCheckedRow"
      />
    </div>

    <n-drawer v-model:show="detailShow" :width="760" placement="right">
      <n-drawer-content title="申诉详情" :native-scrollbar="false">
        <template v-if="detail">
          <n-space :size="12" style="margin-bottom: 16px;" align="center" justify="end">
            <n-button
              v-if="detail.appealStatus === 2"
              type="warning"
              :loading="submittingSupplement"
              @click="supplementMode = !supplementMode"
            >
              {{ supplementMode ? '收起补充材料' : '补充休息证明' }}
            </n-button>
          </n-space>

          <n-alert v-if="detail.appealStatus === 2 && !supplementMode" type="warning" :show-icon="true" style="margin-bottom: 16px;">
            安全专员要求补充材料：<strong>{{ detail.incompleteReason || '请按要求上传完整的休息证明' }}</strong>
            <br />
            请点击右上角「补充休息证明」按钮追加材料，平台会保留每次提交记录。
          </n-alert>

          <n-descriptions :column="2" label-placement="left" bordered size="small">
            <n-descriptions-item label="申诉单号">{{ detail.appealNo }}</n-descriptions-item>
            <n-descriptions-item label="关联限制">{{ detail.restrictionNo }}</n-descriptions-item>
            <n-descriptions-item label="申诉状态" :span="2">
              <n-tag :type="statusTagType(detail.appealStatus!)">
                {{ detail.appealStatusDesc }}
              </n-tag>
              <n-tag v-if="detail.materialComplete === 0" type="error" size="small" style="margin-left: 8px;">
                材料不完整
              </n-tag>
              <n-tag v-else type="success" size="small" style="margin-left: 8px;">材料完整</n-tag>
              <n-text v-if="detail.incompleteReason && detail.appealStatus !== 2" depth="3" style="margin-left: 12px; font-size: 12px;">
                {{ detail.incompleteReason }}
              </n-text>
            </n-descriptions-item>
            <n-descriptions-item label="最近提交时间" :span="2">
              {{ formatTime(detail.submitTime) }}
            </n-descriptions-item>
            <n-descriptions-item label="休息时段" :span="2">
              {{ formatTime(detail.restStartTime) }} — {{ formatTime(detail.restEndTime) }}
            </n-descriptions-item>
            <n-descriptions-item label="休息时长">{{ detail.restMinutes }} 分钟</n-descriptions-item>
            <n-descriptions-item label="证明说明">{{ detail.restProofDesc || '-' }}</n-descriptions-item>
          </n-descriptions>

          <n-divider>申诉理由</n-divider>
          <n-card embedded content-style="white-space: pre-wrap; background: #fafafa;">
            {{ detail.appealReason }}
          </n-card>

          <n-divider v-if="detail.restProofUrl">休息证明文件</n-divider>
          <n-space v-if="detail.restProofUrl" wrap :size="12">
            <template v-for="(url, idx) in proofUrls" :key="idx">
              <a :href="url" target="_blank">
                <n-tag type="info">查看证明文件 {{ idx + 1 }}</n-tag>
              </a>
            </template>
          </n-space>

          <n-divider v-if="detail.auditRemark">初审意见</n-divider>
          <n-card v-if="detail.auditRemark" embedded type="info">
            <template #header>
              审核时间：{{ formatTime(detail.auditTime) }}
            </template>
            {{ detail.auditRemark }}
          </n-card>

          <n-divider v-if="supplementMode">补充材料</n-divider>
          <n-card v-if="supplementMode" embedded type="warning">
            <template #header>
              <n-text strong>追加休息证明（将覆盖或补充原材料）</n-text>
            </template>
            <n-form ref="supplementFormRef" :model="supplementForm" :rules="supplementRules" label-placement="top" label-width="120px">
              <n-grid :cols="2" :x-gap="16">
                <n-form-item label="休息开始时间" path="restStartTime" :span="1">
                  <n-date-picker
                    v-model:value="supplementForm.restStartTime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%;"
                  />
                </n-form-item>
                <n-form-item label="休息结束时间" path="restEndTime" :span="1">
                  <n-date-picker
                    v-model:value="supplementForm.restEndTime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%;"
                    @update:value="calcSupplementMinutes"
                  />
                </n-form-item>
                <n-form-item label="实际休息时长（分钟）" path="restMinutes" :span="1">
                  <n-input-number
                    v-model:value="supplementForm.restMinutes"
                    :min="1"
                    :max="720"
                    placeholder="休息时长"
                    style="width: 100%;"
                  />
                </n-form-item>
                <n-form-item label="休息证明文件" path="restProofUrl" :span="1">
                  <n-upload
                    :show-file-list="true"
                    :max="5"
                    :custom-request="handleSupplementUpload"
                    :on-remove="handleSupplementRemove"
                    accept="image/*,.pdf,.doc,.docx"
                  >
                    <n-button>上传休息证明（图片/PDF等）</n-button>
                    <template #tip>
                      可上传多张，将覆盖或追加到原证明文件
                    </template>
                  </n-upload>
                </n-form-item>
                <n-form-item label="休息证明说明" path="restProofDesc" :span="2">
                  <n-input
                    v-model:value="supplementForm.restProofDesc"
                    type="textarea"
                    :rows="2"
                    placeholder="说明补充证明的内容来源、可验证方式等（可选）"
                  />
                </n-form-item>
                <n-form-item label="补充说明（必填）" path="appealReason" :span="2">
                  <n-input
                    v-model:value="supplementForm.appealReason"
                    type="textarea"
                    :rows="3"
                    placeholder="请说明此次补充了哪些材料、为何之前未提供等补充理由..."
                  />
                </n-form-item>
              </n-grid>
              <n-divider>补充材料自检</n-divider>
              <n-space vertical>
                <n-checkbox v-model:checked="supplementChecklist[0]" disabled>
                  已填写休息时段
                </n-checkbox>
                <n-checkbox v-model:checked="supplementChecklist[1]" disabled>
                  已填写休息时长
                </n-checkbox>
                <n-checkbox v-model:checked="supplementChecklist[2]" disabled>
                  已上传休息证明文件
                </n-checkbox>
                <n-checkbox v-model:checked="supplementChecklist[3]" disabled>
                  已填写补充说明
                </n-checkbox>
              </n-space>
              <div style="text-align: right; margin-top: 16px;">
                <n-button @click="supplementMode = false" style="margin-right: 12px;">取消</n-button>
                <n-button
                  type="primary"
                  :loading="submittingSupplement"
                  :disabled="!supplementComplete"
                  @click="doSupplementSubmit"
                >
                  提交补充材料
                </n-button>
              </div>
            </n-form>
          </n-card>

          <n-divider>材料提交历史</n-divider>
          <n-timeline>
            <n-timeline-item
              v-for="(rec, idx) in detail.materialRecords || []"
              :key="rec.id"
              :type="recordTimelineType(rec.recordType!)"
              :title="rec.recordTypeDesc"
              :time="formatTime(rec.submitTime)"
            >
              <n-space vertical :size="6">
                <n-text v-if="rec.operatorName" depth="3" style="font-size: 12px;">
                  操作人：{{ rec.operatorName }}
                </n-text>
                <n-text v-if="rec.operateRemark" type="warning" style="white-space: pre-wrap;">
                  备注：{{ rec.operateRemark }}
                </n-text>
                <template v-if="rec.recordType !== 3">
                  <n-descriptions :column="2" label-placement="left" size="small" bordered style="max-width: 560px;">
                    <n-descriptions-item label="休息时段" :span="2">
                      {{ formatTime(rec.restStartTime) }} — {{ formatTime(rec.restEndTime) }}
                    </n-descriptions-item>
                    <n-descriptions-item label="休息时长" :span="1">
                      {{ rec.restMinutes ? rec.restMinutes + ' 分钟' : '-' }}
                    </n-descriptions-item>
                    <n-descriptions-item label="证明文件" :span="1">
                      <n-space v-if="rec.restProofUrl" wrap :size="6">
                        <a
                          v-for="(u, i) in splitUrls(rec.restProofUrl)"
                          :key="i"
                          :href="u"
                          target="_blank"
                        >
                          <n-tag size="small" type="info">文件{{ i + 1 }}</n-tag>
                        </a>
                      </n-space>
                      <span v-else>-</span>
                    </n-descriptions-item>
                    <n-descriptions-item label="说明" :span="2">
                      {{ rec.restProofDesc || '-' }}
                    </n-descriptions-item>
                  </n-descriptions>
                  <n-card v-if="rec.appealReason" embedded size="small" content-style="white-space: pre-wrap;">
                    <template #header>申诉/补充说明</template>
                    {{ rec.appealReason }}
                  </n-card>
                </template>
              </n-space>
            </n-timeline-item>
          </n-timeline>
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import {
  listAppealsByDriver,
  supplementAppeal,
  type DriverAppealVO,
  type AppealSupplementDTO
} from '@/api/appeal'
import {
  useMessage,
  type DataTableColumns,
  type FormInst,
  type FormRules,
  type UploadCustomRequestOptions
} from 'naive-ui'
import { h } from 'vue'
import dayjs from 'dayjs'
import { uploadFile } from '@/api/common'

const message = useMessage()
const appStore = useAppStore()
const { currentDriver } = storeToRefs(appStore)

const list = ref<DriverAppealVO[]>([])
const loading = ref(false)
const detail = ref<DriverAppealVO | null>(null)
const detailShow = ref(false)

const supplementMode = ref(false)
const submittingSupplement = ref(false)
const supplementFormRef = ref<FormInst>()
const supplementUploadedUrls = ref<string[]>([])

const supplementForm = ref<AppealSupplementDTO>({
  appealId: 0,
  driverId: currentDriver.value?.id || 1,
  appealReason: '',
  restProofUrl: '',
  restProofDesc: '',
  restStartTime: '',
  restEndTime: '',
  restMinutes: undefined
})

const supplementChecklist = computed(() => [
  !!supplementForm.value.restStartTime && !!supplementForm.value.restEndTime,
  supplementForm.value.restMinutes !== undefined && supplementForm.value.restMinutes > 0,
  !!supplementForm.value.restProofUrl || supplementUploadedUrls.value.length > 0,
  supplementForm.value.appealReason.trim().length > 0
])

const supplementComplete = computed(() => supplementChecklist.value.every(Boolean))

const supplementRules: FormRules = {
  appealReason: { required: true, message: '请填写补充说明理由', trigger: 'blur' },
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

const columns: DataTableColumns<DriverAppealVO> = [
  { title: '申诉单号', key: 'appealNo', width: 160 },
  { title: '关联限制', key: 'restrictionNo', width: 160 },
  {
    title: '状态',
    key: 'appealStatusDesc',
    width: 120,
    render: (row) => h('n-tag', { type: statusTagType(row.appealStatus!) }, row.appealStatusDesc)
  },
  {
    title: '材料',
    key: 'materialComplete',
    width: 100,
    render: (row) =>
      row.materialComplete === 1
        ? h('n-tag', { type: 'success' }, '完整')
        : h('n-tag', { type: 'error' }, '不完整')
  },
  { title: '休息时长', key: 'restMinutes', width: 100, render: (row) => row.restMinutes + ' 分' },
  { title: '最近提交', key: 'submitTime', width: 170, render: (row) => formatTime(row.submitTime) },
  {
    title: '操作',
    key: 'action',
    width: 180,
    render: (row) =>
      h('n-space', null, () => [
        h(
          'n-button',
          { size: 'small', type: 'primary', quaternary: true, onClick: () => showDetail(row) },
          '查看'
        ),
        row.appealStatus === 2
          ? h(
              'n-button',
              {
                size: 'small',
                type: 'warning',
                onClick: () => {
                  showDetail(row)
                  setTimeout(() => (supplementMode.value = true), 120)
                }
              },
              '补充材料'
            )
          : null
      ])
  }
]

const proofUrls = computed(() => {
  if (!detail.value?.restProofUrl) return []
  return splitUrls(detail.value.restProofUrl)
})

function splitUrls(s?: string) {
  if (!s) return []
  return s.split(',').filter(Boolean)
}

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}

function statusTagType(status: number): any {
  const map: Record<number, string> = {
    1: 'warning',
    2: 'error',
    3: 'info',
    4: 'success',
    5: 'default'
  }
  return map[status] || 'default'
}

function recordTimelineType(type: number): any {
  const map: Record<number, string> = {
    1: 'success',
    2: 'info',
    3: 'warning'
  }
  return map[type] || 'default'
}

function onCheckedRow() {}

function showDetail(row: DriverAppealVO) {
  detail.value = row
  detailShow.value = true
  supplementMode.value = false
  supplementUploadedUrls.value = []
  supplementForm.value = {
    appealId: row.id,
    driverId: currentDriver.value?.id || 1,
    appealReason: '',
    restProofUrl: '',
    restProofDesc: '',
    restStartTime: row.restStartTime || '',
    restEndTime: row.restEndTime || '',
    restMinutes: row.restMinutes
  }
}

function calcSupplementMinutes() {
  if (supplementForm.value.restStartTime && supplementForm.value.restEndTime) {
    const m = dayjs(supplementForm.value.restEndTime).diff(
      dayjs(supplementForm.value.restStartTime),
      'minute'
    )
    if (m > 0) supplementForm.value.restMinutes = m
  }
}

watch(
  [() => supplementForm.value.restStartTime, () => supplementForm.value.restEndTime],
  calcSupplementMinutes
)

async function handleSupplementUpload({ file, onFinish, onError }: UploadCustomRequestOptions) {
  try {
    const res = await uploadFile(file.file as File)
    if (res.url) {
      supplementUploadedUrls.value.push(res.url)
      supplementForm.value.restProofUrl = supplementUploadedUrls.value.join(',')
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

function handleSupplementRemove() {
  if (supplementUploadedUrls.value.length > 0) {
    supplementUploadedUrls.value.pop()
    supplementForm.value.restProofUrl = supplementUploadedUrls.value.join(',')
  }
}

async function doSupplementSubmit() {
  try {
    await supplementFormRef.value?.validate()
  } catch (e) {
    return
  }
  if (!detail.value) return
  submittingSupplement.value = true
  try {
    await supplementAppeal(supplementForm.value)
    message.success('补充材料提交成功')
    supplementMode.value = false
    await loadData()
    if (detail.value) {
      const updated = list.value.find((x) => x.id === detail.value!.id)
      if (updated) detail.value = updated
    }
  } catch (e: any) {
    message.error(e.message || '提交失败')
  } finally {
    submittingSupplement.value = false
  }
}

async function loadData() {
  if (!currentDriver.value?.id) return
  loading.value = true
  try {
    list.value = await listAppealsByDriver(currentDriver.value.id)
  } catch (e) {
    message.warning('加载申诉列表失败')
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>

<style scoped>
.card-wrapper {
  padding: 16px 24px;
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #1f2937;
}
</style>
