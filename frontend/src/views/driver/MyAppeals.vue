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

    <n-drawer v-model:show="detailShow" :width="720" placement="right">
      <n-drawer-content title="申诉详情" :native-scrollbar="false">
        <template v-if="detail">
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
              <n-text v-if="detail.incompleteReason" depth="3" style="margin-left: 12px; font-size: 12px;">
                {{ detail.incompleteReason }}
              </n-text>
            </n-descriptions-item>
            <n-descriptions-item label="提交时间" :span="2">
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
        </template>
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { listAppealsByDriver, type DriverAppealVO } from '@/api/appeal'
import { useMessage, type DataTableColumns } from 'naive-ui'
import dayjs from 'dayjs'

const message = useMessage()
const appStore = useAppStore()
const { currentDriver } = storeToRefs(appStore)

const list = ref<DriverAppealVO[]>([])
const loading = ref(false)
const detail = ref<DriverAppealVO | null>(null)
const detailShow = ref(false)

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
  { title: '提交时间', key: 'submitTime', width: 170, render: (row) => formatTime(row.submitTime) },
  {
    title: '操作',
    key: 'action',
    width: 100,
    render: (row) =>
      h(
        'n-button',
        { size: 'small', type: 'primary', quaternary: true, onClick: () => showDetail(row) },
        '查看详情'
      )
  }
]

import { h } from 'vue'

const proofUrls = computed(() => {
  if (!detail.value?.restProofUrl) return []
  return detail.value.restProofUrl.split(',').filter(Boolean)
})

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

function onCheckedRow() {}

function showDetail(row: DriverAppealVO) {
  detail.value = row
  detailShow.value = true
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
