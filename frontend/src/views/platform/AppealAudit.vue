<template>
  <div>
    <div class="card-wrapper">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <div class="section-title" style="margin-bottom: 0;">申诉材料审核</div>
        <div style="display: flex; gap: 12px;">
          <n-select
            v-model:value="query.appealStatus"
            :options="statusOptions"
            clearable
            placeholder="申诉状态"
            style="width: 160px;"
          />
          <n-select
            v-model:value="query.materialComplete"
            :options="materialOptions"
            clearable
            placeholder="材料完整性"
            style="width: 160px;"
          />
          <n-button type="primary" @click="loadData">查询</n-button>
        </div>
      </div>

      <n-alert type="info" :show-icon="true" style="margin-bottom: 16px;">
        审核说明：材料完整性审核（不完整）→ 补充材料后重新审核 → 审核通过（审核中）→ 进入安全复核。材料不完整的申诉不得进入复核流程。
      </n-alert>

      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="true"
        :striped="true"
        :pagination="pagination"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      />
    </div>

    <n-modal
      v-model:show="auditShow"
      preset="card"
      style="width: 860px;"
      title="材料审核"
      :mask-closable="false"
    >
      <template v-if="currentAppeal">
        <n-descriptions :column="2" label-placement="left" bordered size="small">
          <n-descriptions-item label="申诉单号">{{ currentAppeal.appealNo }}</n-descriptions-item>
          <n-descriptions-item label="关联限制">{{ currentAppeal.restrictionNo }}</n-descriptions-item>
          <n-descriptions-item label="司机">
            {{ currentAppeal.driverName }} ({{ currentAppeal.driverCode }})
          </n-descriptions-item>
          <n-descriptions-item label="手机号">{{ currentAppeal.driverPhone }}</n-descriptions-item>
          <n-descriptions-item label="休息时段" :span="2">
            {{ formatTime(currentAppeal.restStartTime) }} —
            {{ formatTime(currentAppeal.restEndTime) }}
          </n-descriptions-item>
          <n-descriptions-item label="休息时长">{{ currentAppeal.restMinutes }} 分钟</n-descriptions-item>
          <n-descriptions-item label="提交时间">{{ formatTime(currentAppeal.submitTime) }}</n-descriptions-item>
        </n-descriptions>
        <n-divider>申诉理由</n-divider>
        <n-card embedded style="background: #fafafa; white-space: pre-wrap;">
          {{ currentAppeal.appealReason }}
        </n-card>
        <n-divider>证明材料</n-divider>
        <n-space vertical :size="12">
          <div>
            <n-text v-if="currentAppeal.restProofUrl" depth="2">
              文件：
              <a v-for="(u, i) in proofUrls" :key="i" :href="u" target="_blank" style="margin-right: 12px;">
                <n-tag type="info">证明文件{{ i + 1 }}</n-tag>
              </a>
            </n-text>
            <n-tag v-else type="error">未上传证明文件</n-tag>
          </div>
          <div>
            <n-text depth="2">说明：{{ currentAppeal.restProofDesc || '无' }}</n-text>
          </div>
        </n-space>

        <n-divider>审核操作</n-divider>
        <n-form label-placement="top" label-width="120px">
          <n-form-item label="材料完整性判定">
            <n-radio-group v-model:value="materialForm.materialComplete">
              <n-radio :value="1">✅ 完整，可进入安全复核</n-radio>
              <n-radio :value="0">❌ 不完整，退回补充</n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item v-if="materialForm.materialComplete === 0" label="不完整原因（退回司机时显示）">
            <n-input
              v-model:value="materialForm.incompleteReason"
              type="textarea"
              :rows="3"
              placeholder="请说明缺少哪些材料，例如：缺少休息场所照片、休息时段无佐证等..."
            />
          </n-form-item>
          <n-form-item label="审核意见">
            <n-input
              v-model:value="materialForm.auditRemark"
              type="textarea"
              :rows="2"
              placeholder="请填写审核意见（可选）"
            />
          </n-form-item>
        </n-form>

        <div style="text-align: right; margin-top: 8px;">
          <n-button @click="auditShow = false">取消</n-button>
          <n-button type="primary" :loading="saving" @click="doAudit" style="margin-left: 12px;">
            提交审核
          </n-button>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import { appealPage, materialCheck, type AppealQueryDTO, type DriverAppealVO } from '@/api/appeal'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { useMessage, type DataTableColumns } from 'naive-ui'

const message = useMessage()
const appStore = useAppStore()
const { currentReviewer } = storeToRefs(appStore)

const loading = ref(false)
const list = ref<DriverAppealVO[]>([])
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })
const query = reactive<AppealQueryDTO>({
  pageNum: 1,
  pageSize: 10
})

const statusOptions = [
  { label: '待审核', value: 1 },
  { label: '材料不完整', value: 2 },
  { label: '审核中', value: 3 },
  { label: '申诉通过', value: 4 },
  { label: '申诉驳回', value: 5 }
]
const materialOptions = [
  { label: '完整', value: 1 },
  { label: '不完整', value: 0 }
]

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}
function statusTag(s: number): any {
  return ({ 1: 'warning', 2: 'error', 3: 'info', 4: 'success', 5: 'default' } as any)[s] || 'default'
}

const columns: DataTableColumns<DriverAppealVO> = [
  { title: '申诉单号', key: 'appealNo', width: 150 },
  { title: '限制单号', key: 'restrictionNo', width: 150 },
  { title: '司机', key: 'driverName', width: 110, render: (row) => `${row.driverName}(${row.driverCode})` },
  {
    title: '申诉状态',
    key: 'appealStatusDesc',
    width: 110,
    render: (row) => h('n-tag', { type: statusTag(row.appealStatus!) }, row.appealStatusDesc)
  },
  {
    title: '材料',
    key: 'materialComplete',
    width: 90,
    render: (row) =>
      row.materialComplete === 1
        ? h('n-tag', { type: 'success' }, '完整')
        : h('n-tag', { type: 'error' }, '不完整')
  },
  { title: '休息时长', key: 'restMinutes', width: 100, render: (row) => row.restMinutes + ' 分' },
  {
    title: '不完整原因',
    key: 'incompleteReason',
    width: 180,
    ellipsis: { tooltip: true },
    render: (row) => row.incompleteReason || '-'
  },
  { title: '提交时间', key: 'submitTime', width: 160, render: (row) => formatTime(row.submitTime) },
  {
    title: '操作',
    key: 'action',
    width: 100,
    render: (row) => {
      const disabled = row.appealStatus === 4 || row.appealStatus === 5
      return h(
        'n-button',
        {
          size: 'small',
          type: 'primary',
          disabled,
          onClick: () => openAudit(row)
        },
        disabled ? '已完成' : '审核'
      )
    }
  }
]

const auditShow = ref(false)
const saving = ref(false)
const currentAppeal = ref<DriverAppealVO | null>(null)
const materialForm = reactive({
  appealId: 0,
  materialComplete: 1 as number,
  incompleteReason: '',
  auditorId: currentReviewer.value.id,
  auditRemark: ''
})

const proofUrls = computed(() => {
  if (!currentAppeal.value?.restProofUrl) return []
  return currentAppeal.value.restProofUrl.split(',').filter(Boolean)
})

function openAudit(row: DriverAppealVO) {
  currentAppeal.value = row
  materialForm.appealId = row.id!
  materialForm.materialComplete = row.materialComplete
  materialForm.incompleteReason = row.incompleteReason || ''
  materialForm.auditorId = currentReviewer.value.id
  materialForm.auditRemark = row.auditRemark || ''
  auditShow.value = true
}

async function doAudit() {
  if (materialForm.materialComplete === 0 && !materialForm.incompleteReason?.trim()) {
    message.warning('请说明材料不完整的原因')
    return
  }
  saving.value = true
  try {
    await materialCheck({ ...materialForm })
    message.success('审核提交成功')
    auditShow.value = false
    loadData()
  } catch (e: any) {
    message.error(e.message || '提交失败')
  } finally {
    saving.value = false
  }
}

function handlePageChange(p: number) {
  pagination.page = p
  query.pageNum = p
  loadData()
}
function handlePageSizeChange(s: number) {
  pagination.pageSize = s
  pagination.page = 1
  query.pageSize = s
  query.pageNum = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    const res = await appealPage(query)
    list.value = res.records
    pagination.itemCount = res.total
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>
