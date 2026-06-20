<template>
  <div>
    <div class="stats-row">
      <n-card class="stat-card" embedded>
        <div class="stat-label">限制中</div>
        <div class="stat-value" style="color: #d03050;">{{ counts.restricting }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">申诉中</div>
        <div class="stat-value" style="color: #2080f0;">{{ counts.appealing }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">已解除</div>
        <div class="stat-value" style="color: #18a058;">{{ counts.released }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">已维持</div>
        <div class="stat-value" style="color: #f0a020;">{{ counts.maintained }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">高风险</div>
        <div class="stat-value" style="color: #d03050;">{{ counts.highRisk }}</div>
      </n-card>
    </div>

    <div class="card-wrapper">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <div class="section-title" style="margin-bottom: 0;">风控限制列表</div>
        <div style="display: flex; gap: 12px;">
          <n-select
            v-model:value="query.driverId"
            :options="driverOptions"
            filterable
            clearable
            placeholder="选择司机"
            style="width: 220px;"
          />
          <n-select
            v-model:value="query.restrictionStatus"
            :options="statusOptions"
            clearable
            placeholder="限制状态"
            style="width: 140px;"
          />
          <n-select
            v-model:value="query.restrictionType"
            :options="typeOptions"
            clearable
            placeholder="限制类型"
            style="width: 140px;"
          />
          <n-button type="primary" @click="loadData">查询</n-button>
        </div>
      </div>

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

    <n-modal v-model:show="detailShow" preset="card" style="width: 820px;" title="风控详情">
      <template v-if="detail">
        <n-descriptions :column="2" label-placement="left" bordered size="small">
          <n-descriptions-item label="限制单号">{{ detail.restrictionNo }}</n-descriptions-item>
          <n-descriptions-item label="限制类型">
            <n-tag type="info">{{ detail.restrictionTypeDesc }}</n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="风险等级">
            <n-tag :type="riskTagType(detail.riskLevel!)">{{ detail.riskLevelDesc }}</n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="状态">
            <n-tag :type="statusTagType(detail.restrictionStatus!)">{{ detail.restrictionStatusDesc }}</n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="司机">{{ detail.driverName }} ({{ detail.driverCode }})</n-descriptions-item>
          <n-descriptions-item label="手机号">{{ detail.driverPhone }}</n-descriptions-item>
          <n-descriptions-item label="触发时间">{{ formatTime(detail.startTime) }}</n-descriptions-item>
          <n-descriptions-item label="结束时间">{{ formatTime(detail.endTime) }}</n-descriptions-item>
          <n-descriptions-item label="实际值 / 阈值" :span="2">
            <n-text type="error" strong>{{ detail.triggerValue }}</n-text>
            <n-text style="margin: 0 6px;">/</n-text>
            <n-text>{{ detail.thresholdValue }}</n-text>
          </n-descriptions-item>
          <n-descriptions-item label="触发原因" :span="2">
            <n-alert type="warning" :show-icon="true">
              {{ detail.triggerReason }}
            </n-alert>
          </n-descriptions-item>
        </n-descriptions>
        <n-divider>原始风控依据（保留）</n-divider>
        <n-code :code="evidenceJson" language="json" word-wrap height="280" />
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import { restrictionPage, type RiskRestrictionVO, type RestrictionQueryDTO } from '@/api/risk'
import { listDrivers, type Driver } from '@/api/driver'
import { useMessage, type DataTableColumns } from 'naive-ui'

const message = useMessage()

const loading = ref(false)
const list = ref<RiskRestrictionVO[]>([])
const pagination = reactive({ page: 1, pageSize: 10, itemCount: 0 })
const query = reactive<RestrictionQueryDTO>({
  pageNum: 1,
  pageSize: 10
})

const counts = reactive({ restricting: 0, appealing: 0, released: 0, maintained: 0, highRisk: 0 })

const drivers = ref<Driver[]>([])
const driverOptions = computed(() =>
  drivers.value.map((d) => ({
    label: `${d.driverName} (${d.driverCode})`,
    value: d.id
  }))
)

const statusOptions = [
  { label: '限制中', value: 1 },
  { label: '申诉中', value: 2 },
  { label: '已解除', value: 3 },
  { label: '已维持', value: 4 }
]
const typeOptions = [
  { label: '连续接单超限', value: 1 },
  { label: '在线时长超限', value: 2 },
  { label: '连续工作超时', value: 3 }
]

const detailShow = ref(false)
const detail = ref<RiskRestrictionVO | null>(null)
const evidenceJson = ref('')

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}
function riskTagType(l: number): any {
  return ({ 1: 'success', 2: 'warning', 3: 'error' } as any)[l] || 'default'
}
function statusTagType(s: number): any {
  return ({ 1: 'error', 2: 'info', 3: 'success', 4: 'warning' } as any)[s] || 'default'
}

const columns: DataTableColumns<RiskRestrictionVO> = [
  { title: '单号', key: 'restrictionNo', width: 150 },
  { title: '司机', key: 'driverName', width: 110, render: (row) => `${row.driverName}(${row.driverCode})` },
  { title: '手机号', key: 'driverPhone', width: 120 },
  { title: '类型', key: 'restrictionTypeDesc', width: 120, render: (row) => h('n-tag', { type: 'info' }, row.restrictionTypeDesc) },
  {
    title: '风险',
    key: 'riskLevelDesc',
    width: 90,
    render: (row) => h('n-tag', { type: riskTagType(row.riskLevel!) }, row.riskLevelDesc)
  },
  {
    title: '状态',
    key: 'restrictionStatusDesc',
    width: 100,
    render: (row) => h('n-tag', { type: statusTagType(row.restrictionStatus!) }, row.restrictionStatusDesc)
  },
  { title: '原因', key: 'triggerReason', width: 260, ellipsis: { tooltip: true } },
  { title: '触发时间', key: 'startTime', width: 160, render: (row) => formatTime(row.startTime) },
  {
    title: '操作',
    key: 'action',
    width: 90,
    render: (row) =>
      h(
        'n-button',
        { size: 'small', type: 'primary', quaternary: true, onClick: () => viewDetail(row) },
        '详情'
      )
  }
]

function viewDetail(row: RiskRestrictionVO) {
  detail.value = row
  evidenceJson.value = row.evidenceData ? JSON.stringify(JSON.parse(row.evidenceData), null, 2) : '{}'
  detailShow.value = true
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
    const res = await restrictionPage(query)
    list.value = res.records
    pagination.itemCount = res.total
    counts.restricting = list.value.filter((x) => x.restrictionStatus === 1).length
    counts.appealing = list.value.filter((x) => x.restrictionStatus === 2).length
    counts.released = list.value.filter((x) => x.restrictionStatus === 3).length
    counts.maintained = list.value.filter((x) => x.restrictionStatus === 4).length
    counts.highRisk = list.value.filter((x) => x.riskLevel === 3).length
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  try {
    drivers.value = await listDrivers()
  } catch (e) {}
  loadData()
})
</script>

<style scoped>
.stats-row {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
}
</style>
