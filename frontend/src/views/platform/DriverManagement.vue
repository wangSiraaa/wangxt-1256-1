<template>
  <div>
    <div class="card-wrapper">
      <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px;">
        <div class="section-title" style="margin-bottom: 0;">司机管理</div>
        <div style="display: flex; gap: 12px;">
          <n-input
            v-model:value="keyword"
            clearable
            placeholder="搜索工号/姓名/手机号"
            style="width: 260px;"
            @keydown.enter="loadDrivers"
          />
          <n-button type="primary" @click="loadDrivers">搜索</n-button>
        </div>
      </div>

      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="true"
        :striped="true"
      />
    </div>

    <n-modal v-model:show="statsShow" preset="card" style="width: 760px;" :title="statsTitle">
      <template v-if="statsDriver">
        <n-descriptions :column="2" label-placement="left" bordered size="small">
          <n-descriptions-item label="工号">{{ statsDriver.driverCode }}</n-descriptions-item>
          <n-descriptions-item label="姓名">{{ statsDriver.driverName }}</n-descriptions-item>
          <n-descriptions-item label="手机号">{{ statsDriver.phone }}</n-descriptions-item>
          <n-descriptions-item label="驾驶证">{{ statsDriver.licenseNo }}</n-descriptions-item>
        </n-descriptions>
        <n-divider>近30日数据概览</n-divider>
        <n-space vertical>
          <div style="font-weight: 600;">风控限制记录：</div>
          <n-data-table
            :columns="rColumns"
            :data="driverRestrictions"
            :bordered="true"
            size="small"
            :max-height="260"
          />
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, ref } from 'vue'
import dayjs from 'dayjs'
import { historyRestrictions, type RiskRestrictionVO } from '@/api/risk'
import { searchDrivers, type Driver } from '@/api/driver'
import { useMessage, type DataTableColumns } from 'naive-ui'

const message = useMessage()
const keyword = ref('')
const loading = ref(false)
const list = ref<Driver[]>([])

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}

const columns: DataTableColumns<Driver> = [
  { title: '工号', key: 'driverCode', width: 120 },
  { title: '姓名', key: 'driverName', width: 100 },
  { title: '手机号', key: 'phone', width: 140 },
  { title: '驾驶证号', key: 'licenseNo', width: 200 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) =>
      h('n-tag', { type: row.status === 1 ? 'success' : 'error' }, row.status === 1 ? '正常' : '禁用')
  },
  {
    title: '操作',
    key: 'action',
    width: 160,
    render: (row) =>
      h('n-button', { size: 'small', type: 'primary', onClick: () => viewStats(row) }, '查看风控记录')
  }
]

const statsShow = ref(false)
const statsDriver = ref<Driver | null>(null)
const statsTitle = computed(() =>
  statsDriver.value ? statsDriver.value.driverName + ' 的风控记录' : ''
)
const driverRestrictions = ref<RiskRestrictionVO[]>([])

const rColumns: DataTableColumns<RiskRestrictionVO> = [
  { title: '单号', key: 'restrictionNo', width: 150 },
  { title: '类型', key: 'restrictionTypeDesc', width: 120 },
  {
    title: '风险',
    key: 'riskLevelDesc',
    width: 90,
    render: (row) =>
      h(
        'n-tag',
        { type: ({ 1: 'success', 2: 'warning', 3: 'error' } as any)[row.riskLevel!] },
        row.riskLevelDesc
      )
  },
  {
    title: '状态',
    key: 'restrictionStatusDesc',
    width: 100,
    render: (row) =>
      h(
        'n-tag',
        { type: ({ 1: 'error', 2: 'info', 3: 'success', 4: 'warning' } as any)[row.restrictionStatus!] },
        row.restrictionStatusDesc
      )
  },
  { title: '原因', key: 'triggerReason', ellipsis: { tooltip: true } },
  { title: '触发时间', key: 'startTime', width: 160, render: (row) => formatTime(row.startTime) }
]

async function viewStats(d: Driver) {
  statsDriver.value = d
  statsShow.value = true
  try {
    driverRestrictions.value = await historyRestrictions(d.id!)
  } catch (e) {
    driverRestrictions.value = []
  }
}

async function loadDrivers() {
  loading.value = true
  try {
    list.value = await searchDrivers(keyword.value)
  } catch (e) {
    list.value = []
  } finally {
    loading.value = false
  }
}

onMounted(loadDrivers)
</script>
