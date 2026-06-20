<template>
  <div>
    <div class="card-wrapper">
      <div class="section-title">复核记录</div>

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

    <n-modal v-model:show="detailShow" preset="card" style="width: 860px;" title="复核详情">
      <template v-if="detail">
        <n-descriptions :column="2" label-placement="left" bordered size="small">
          <n-descriptions-item label="复核单号">{{ detail.reviewNo }}</n-descriptions-item>
          <n-descriptions-item label="申诉单号">{{ detail.appealNo }}</n-descriptions-item>
          <n-descriptions-item label="限制单号">{{ detail.restrictionNo }}</n-descriptions-item>
          <n-descriptions-item label="司机">
            {{ detail.driverName }} ({{ detail.driverCode }})
          </n-descriptions-item>
          <n-descriptions-item label="复核员">{{ detail.reviewerName }}</n-descriptions-item>
          <n-descriptions-item label="复核时间">{{ formatTime(detail.reviewTime) }}</n-descriptions-item>
          <n-descriptions-item label="复核结果" :span="2">
            <n-tag :type="detail.reviewResult === 1 ? 'success' : 'warning'">
              {{ detail.reviewResultDesc }}
            </n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="核实休息时长">
            {{ detail.restVerifiedMinutes }} 分钟
          </n-descriptions-item>
          <n-descriptions-item label="原始风控依据">
            <n-tag :type="detail.keepEvidence === 1 ? 'success' : 'error'">
              {{ detail.keepEvidence === 1 ? '已保留' : '未保留' }}
            </n-tag>
          </n-descriptions-item>
        </n-descriptions>
        <n-divider>复核意见</n-divider>
        <n-card embedded style="background: #fafafa; white-space: pre-wrap;">
          {{ detail.reviewRemark }}
        </n-card>
        <n-divider v-if="detail.verifyEvidence">核实材料</n-divider>
        <n-card v-if="detail.verifyEvidence" embedded>
          <n-code :code="detail.verifyEvidence" language="json" word-wrap height="160" />
        </n-card>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { h, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import { reviewPage, reviewDetail, type SafetyReviewVO } from '@/api/review'
import { useMessage, type DataTableColumns } from 'naive-ui'

const message = useMessage()

const loading = ref(false)
const list = ref<SafetyReviewVO[]>([])
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}

const columns: DataTableColumns<SafetyReviewVO> = [
  { title: '复核单号', key: 'reviewNo', width: 150 },
  { title: '申诉单号', key: 'appealNo', width: 150 },
  { title: '限制单号', key: 'restrictionNo', width: 150 },
  { title: '司机', key: 'driverName', width: 110, render: (row) => `${row.driverName}(${row.driverCode})` },
  {
    title: '复核结果',
    key: 'reviewResultDesc',
    width: 110,
    render: (row) =>
      h('n-tag', { type: row.reviewResult === 1 ? 'success' : 'warning' }, row.reviewResultDesc)
  },
  { title: '核实休息(分)', key: 'restVerifiedMinutes', width: 110, render: (row) =>
    row.restVerifiedMinutes != null ? row.restVerifiedMinutes : '-'
  },
  {
    title: '依据保留',
    key: 'keepEvidence',
    width: 100,
    render: (row) =>
      h('n-tag', { type: row.keepEvidence === 1 ? 'success' : 'error' }, row.keepEvidence === 1 ? '已保留' : '未保留')
  },
  { title: '复核员', key: 'reviewerName', width: 100 },
  { title: '复核时间', key: 'reviewTime', width: 160, render: (row) => formatTime(row.reviewTime) },
  {
    title: '操作',
    key: 'action',
    width: 90,
    render: (row) =>
      h('n-button', { size: 'small', type: 'primary', quaternary: true, onClick: () => openDetail(row) }, '详情')
  }
]

const detailShow = ref(false)
const detail = ref<SafetyReviewVO | null>(null)

async function openDetail(row: SafetyReviewVO) {
  try {
    detail.value = await reviewDetail(row.id!)
    detailShow.value = true
  } catch (e) {
    detail.value = row
    detailShow.value = true
  }
}

function handlePageChange(p: number) {
  pagination.page = p
  loadData()
}
function handlePageSizeChange(s: number) {
  pagination.pageSize = s
  pagination.page = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    const res = await reviewPage({ pageNum: pagination.page, pageSize: pagination.pageSize })
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
