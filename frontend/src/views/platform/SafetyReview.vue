<template>
  <div>
    <div class="card-wrapper">
      <div class="section-title">安全复核（最终决策）</div>
      <n-alert type="warning" :show-icon="true" style="margin-bottom: 16px;">
        说明：仅 <n-tag size="small" type="info">审核中（材料完整）</n-tag>
        的申诉可进入本流程。复核员需核实休息证明的有效性，决定
        <n-tag size="small" type="success">解除限制</n-tag> 或
        <n-tag size="small" type="warning">维持限制</n-tag>。
        解除限制后系统 <strong>自动保留原始风控依据</strong>。
      </n-alert>

      <n-data-table
        :columns="columns"
        :data="auditingList"
        :loading="loading"
        :bordered="true"
        :striped="true"
        size="small"
      />
    </div>

    <n-modal
      v-model:show="reviewShow"
      preset="card"
      style="width: 900px;"
      title="安全复核决策"
      :mask-closable="false"
    >
      <template v-if="currentAppeal">
        <n-grid :cols="2" :x-gap="16">
          <n-card embedded style="margin-bottom: 16px;">
            <template #header>🧾 申诉信息</template>
            <n-descriptions :column="1" label-placement="left" size="small">
              <n-descriptions-item label="申诉单号">{{ currentAppeal.appealNo }}</n-descriptions-item>
              <n-descriptions-item label="司机">
                {{ currentAppeal.driverName }} ({{ currentAppeal.driverCode }}) /
                {{ currentAppeal.driverPhone }}
              </n-descriptions-item>
              <n-descriptions-item label="休息时段">
                {{ formatTime(currentAppeal.restStartTime) }} —
                {{ formatTime(currentAppeal.restEndTime) }}
              </n-descriptions-item>
              <n-descriptions-item label="申报休息时长">
                <n-text type="success" strong>{{ currentAppeal.restMinutes }} 分钟</n-text>
              </n-descriptions-item>
              <n-descriptions-item label="提交时间">{{ formatTime(currentAppeal.submitTime) }}</n-descriptions-item>
              <n-descriptions-item label="证明">
                <template v-for="(u, i) in proofUrls" :key="i">
                  <a :href="u" target="_blank" style="margin-right: 8px;">
                    <n-tag size="small" type="info">文件{{ i + 1 }}</n-tag>
                  </a>
                </template>
                <span v-if="!proofUrls.length">无</span>
              </n-descriptions-item>
            </n-descriptions>
            <n-divider style="margin: 8px 0;" />
            <n-text depth="3" style="font-size: 12px;">申诉理由：</n-text>
            <div style="font-size: 13px; margin-top: 4px;">{{ currentAppeal.appealReason }}</div>
          </n-card>

          <n-card embedded style="margin-bottom: 16px;">
            <template #header>🚫 原始风控依据</template>
            <n-descriptions :column="1" label-placement="left" size="small" v-if="restriction">
              <n-descriptions-item label="限制单号">{{ restriction.restrictionNo }}</n-descriptions-item>
              <n-descriptions-item label="类型">{{ restriction.restrictionTypeDesc }}</n-descriptions-item>
              <n-descriptions-item label="触发原因">
                <n-text type="error">{{ restriction.triggerReason }}</n-text>
              </n-descriptions-item>
              <n-descriptions-item label="实际 / 阈值">
                <n-text type="error" strong>{{ restriction.triggerValue }}</n-text>
                / {{ restriction.thresholdValue }}
              </n-descriptions-item>
              <n-descriptions-item label="风险等级">{{ restriction.riskLevelDesc }}</n-descriptions-item>
              <n-descriptions-item label="触发时间">{{ formatTime(restriction.startTime) }}</n-descriptions-item>
            </n-descriptions>
            <n-divider style="margin: 8px 0;" />
            <n-collapse>
              <n-collapse-item title="查看完整判定数据（保留）" name="evidence">
                <n-code :code="evidenceJson" language="json" word-wrap height="220" />
              </n-collapse-item>
            </n-collapse>
          </n-card>
        </n-grid>

        <n-divider>复核决策</n-divider>
        <n-form label-placement="top">
          <n-form-item label="核实后的有效休息时长（分钟）">
            <n-input-number v-model:value="reviewForm.restVerifiedMinutes" :min="0" :max="720" />
            <n-text depth="3" style="font-size: 12px; margin-left: 12px;">
              阈值：{{ threshold.requireRestMinutes }} 分钟（≥ 阈值可判解除）
            </n-text>
          </n-form-item>
          <n-form-item label="复核结论">
            <n-radio-group v-model:value="reviewForm.reviewResult">
              <n-radio :value="1">
                <n-tag type="success">解除限制</n-tag>
                &nbsp;核实休息有效，解除接单限制
              </n-radio>
              <n-radio :value="2">
                <n-tag type="warning">维持限制</n-tag>
                &nbsp;休息证明不充分或未达阈值
              </n-radio>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="核实材料记录（JSON备注）">
            <n-input
              v-model:value="reviewForm.verifyEvidence"
              type="textarea"
              :rows="2"
              placeholder="可填写核实记录：如休息照片时间戳验证、GPS轨迹核对、电话核实情况等（JSON/文本）"
            />
          </n-form-item>
          <n-form-item label="复核意见（必填）">
            <n-input
              v-model:value="reviewForm.reviewRemark"
              type="textarea"
              :rows="3"
              placeholder="请详细说明复核依据和结论..."
            />
          </n-form-item>
          <n-form-item label="是否保留原始风控依据">
            <n-switch v-model:value="keepEvidence" />
            <n-text depth="3" style="margin-left: 8px; font-size: 12px;">
              按系统要求，解除限制后仍须保留原始风控依据（默认开启）
            </n-text>
          </n-form-item>
        </n-form>

        <div style="text-align: right; margin-top: 8px;">
          <n-button @click="reviewShow = false">取消</n-button>
          <n-button
            type="primary"
            :type="reviewForm.reviewResult === 1 ? 'success' : 'warning'"
            :loading="saving"
            @click="doReview"
            style="margin-left: 12px;"
          >
            提交复核结论
          </n-button>
        </div>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import { appealPage, type DriverAppealVO } from '@/api/appeal'
import { restrictionDetail, type RiskRestrictionVO } from '@/api/risk'
import { doReview, type SafetyReviewDTO } from '@/api/review'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { useMessage, type DataTableColumns } from 'naive-ui'

const message = useMessage()
const appStore = useAppStore()
const { currentReviewer, threshold } = storeToRefs(appStore)

const loading = ref(false)
const list = ref<DriverAppealVO[]>([])

const auditingList = computed(() => list.value.filter((x) => x.appealStatus === 3))

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}

const columns: DataTableColumns<DriverAppealVO> = [
  { title: '申诉单号', key: 'appealNo', width: 150 },
  { title: '限制单号', key: 'restrictionNo', width: 150 },
  { title: '司机', key: 'driverName', width: 110, render: (row) => `${row.driverName}(${row.driverCode})` },
  { title: '申报休息', key: 'restMinutes', width: 100, render: (row) => row.restMinutes + ' 分' },
  { title: '证明文件', key: 'proof', width: 120, render: (row) =>
    row.restProofUrl
      ? h('n-tag', { type: 'success', size: 'small' }, '已上传 ' + row.restProofUrl.split(',').filter(Boolean).length + ' 个')
      : h('n-tag', { type: 'error', size: 'small' }, '无')
  },
  { title: '提交时间', key: 'submitTime', width: 160, render: (row) => formatTime(row.submitTime) },
  {
    title: '操作',
    key: 'action',
    width: 120,
    render: (row) =>
      h('n-button', { size: 'small', type: 'primary', onClick: () => openReview(row) }, '开始复核')
  }
]

const reviewShow = ref(false)
const saving = ref(false)
const currentAppeal = ref<DriverAppealVO | null>(null)
const restriction = ref<RiskRestrictionVO | null>(null)
const evidenceJson = ref('{}')
const keepEvidence = ref(true)

const proofUrls = computed(() => {
  if (!currentAppeal.value?.restProofUrl) return []
  return currentAppeal.value.restProofUrl.split(',').filter(Boolean)
})

const reviewForm = reactive<SafetyReviewDTO>({
  appealId: 0,
  restrictionId: 0,
  reviewResult: 1,
  reviewRemark: '',
  reviewerId: currentReviewer.value.id,
  reviewerName: currentReviewer.value.name,
  verifyEvidence: '',
  restVerifiedMinutes: 0,
  keepEvidence: 1
})

async function openReview(row: DriverAppealVO) {
  currentAppeal.value = row
  reviewForm.appealId = row.id!
  reviewForm.restrictionId = row.restrictionId!
  reviewForm.restVerifiedMinutes = row.restMinutes
  reviewForm.reviewRemark = ''
  reviewForm.verifyEvidence = ''
  reviewForm.reviewerId = currentReviewer.value.id
  reviewForm.reviewerName = currentReviewer.value.name
  reviewForm.keepEvidence = 1
  keepEvidence.value = true
  try {
    restriction.value = await restrictionDetail(row.restrictionId!)
    evidenceJson.value = restriction.value.evidenceData
      ? JSON.stringify(JSON.parse(restriction.value.evidenceData), null, 2)
      : '{}'
  } catch (e) {
    restriction.value = null
  }
  reviewShow.value = true
}

async function doReview() {
  if (!reviewForm.reviewRemark.trim()) {
    message.warning('请填写复核意见')
    return
  }
  reviewForm.keepEvidence = keepEvidence.value ? 1 : 0
  saving.value = true
  try {
    await doReview({ ...reviewForm })
    message.success(
      reviewForm.reviewResult === 1 ? '已解除限制，原始依据已保留' : '已维持限制'
    )
    reviewShow.value = false
    loadData()
  } catch (e: any) {
    message.error(e.message || '提交失败')
  } finally {
    saving.value = false
  }
}

async function loadData() {
  loading.value = true
  try {
    const query = { pageNum: 1, pageSize: 999, appealStatus: 3, materialComplete: 1 }
    const res = await appealPage(query)
    list.value = res.records
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadData)
</script>
