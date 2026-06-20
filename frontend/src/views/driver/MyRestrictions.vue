<template>
  <div>
    <div class="stats-row">
      <n-card class="stat-card" embedded>
        <div class="stat-label">当前生效限制</div>
        <div class="stat-value" style="color: #d03050;">{{ activeList.length }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">历史限制记录</div>
        <div class="stat-value" style="color: #2080f0;">{{ historyList.length }}</div>
      </n-card>
      <n-card class="stat-card" embedded>
        <div class="stat-label">风控阈值</div>
        <div class="stat-threshold">
          <n-tag size="small" type="warning">连续{{ threshold.maxConsecutiveOrders }}单</n-tag>
          <n-tag size="small" type="warning">在线{{ threshold.maxOnlineHours }}时</n-tag>
          <n-tag size="small" type="warning">连续{{ threshold.consecutiveHoursThreshold }}时</n-tag>
        </div>
      </n-card>
    </div>

    <div v-if="activeList.length > 0" class="card-wrapper">
      <div class="section-title">⚠️ 当前生效限制（{{ activeList.length }}条）</div>
      <n-space vertical :size="16">
        <n-card v-for="r in activeList" :key="r.id" hoverable>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <div>
                <n-tag :type="getRiskTagType(r.riskLevel!)" size="small">
                  {{ r.riskLevelDesc }}
                </n-tag>
                <n-tag type="error" size="small" style="margin-left: 8px;">
                  {{ r.restrictionStatusDesc }}
                </n-tag>
                <span style="margin-left: 12px; font-weight: 500;">{{ r.restrictionTypeDesc }}</span>
              </div>
              <n-button
                v-if="!r.appealId"
                type="primary"
                size="small"
                @click="gotoAppeal(r.id!)"
              >
                提交申诉
              </n-button>
              <n-tag v-else type="info" size="small">
                已申诉，等待处理
              </n-tag>
            </div>
          </template>
          <n-descriptions :column="2" label-placement="left" bordered size="small">
            <n-descriptions-item label="限制单号">{{ r.restrictionNo }}</n-descriptions-item>
            <n-descriptions-item label="触发时间">
              {{ formatTime(r.startTime) }}
            </n-descriptions-item>
            <n-descriptions-item label="触发原因" :span="2">
              <n-alert type="warning" :show-icon="true">
                {{ r.triggerReason }}
              </n-alert>
            </n-descriptions-item>
            <n-descriptions-item label="实际值 / 阈值">
              <n-text type="error" strong>{{ r.triggerValue }}</n-text>
              <n-text style="margin: 0 6px;">/</n-text>
              <n-text>{{ r.thresholdValue }}</n-text>
            </n-descriptions-item>
          </n-descriptions>
          <div style="margin-top: 12px;">
            <n-button quaternary size="small" @click="viewEvidence(r.evidenceData, r.restrictionNo!)">
              查看原始风控依据
            </n-button>
          </div>
        </n-card>
      </n-space>
    </div>

    <div v-else class="card-wrapper">
      <n-result status="success" title="当前无限制" description="您的工作状态良好，请继续保持安全驾驶！">
        <template #footer>
          <n-button @click="loadData">刷新状态</n-button>
        </template>
      </n-result>
    </div>

    <div class="card-wrapper">
      <div class="section-title">历史限制记录</div>
      <n-data-table
        :columns="historyColumns"
        :data="historyList"
        :bordered="true"
        :striped="true"
        size="small"
      />
    </div>

    <n-modal v-model:show="evidenceModalShow" preset="card" style="width: 720px;" title="原始风控依据">
      <n-spin :show="loadingEvidence">
        <n-code :code="evidenceJson" language="json" word-wrap height="400" />
      </n-spin>
    </n-modal>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { activeRestrictions, historyRestrictions, type RiskRestrictionVO } from '@/api/risk'
import { useMessage, type DataTableColumns } from 'naive-ui'
import dayjs from 'dayjs'

const router = useRouter()
const message = useMessage()
const appStore = useAppStore()
const { threshold, currentDriver } = storeToRefs(appStore)
const driverId = computed(() => currentDriver.value?.id)

const activeList = ref<RiskRestrictionVO[]>([])
const historyList = ref<RiskRestrictionVO[]>([])

const historyColumns: DataTableColumns<RiskRestrictionVO> = [
  { title: '单号', key: 'restrictionNo', width: 160 },
  { title: '类型', key: 'restrictionTypeDesc', width: 120 },
  { title: '原因', key: 'triggerReason', ellipsis: { tooltip: true } },
  {
    title: '风险等级',
    key: 'riskLevelDesc',
    width: 100,
    render: (row) => {
      const typeMap: Record<number, string> = { 1: 'success', 2: 'warning', 3: 'error' }
      return h('n-tag', { type: (typeMap[row.riskLevel!] as any) || 'default' }, row.riskLevelDesc)
    }
  },
  {
    title: '状态',
    key: 'restrictionStatusDesc',
    width: 100,
    render: (row) => {
      const statusMap: Record<number, string> = { 1: 'error', 2: 'info', 3: 'success', 4: 'warning' }
      return h('n-tag', { type: (statusMap[row.restrictionStatus!] as any) || 'default' }, row.restrictionStatusDesc)
    }
  },
  { title: '触发时间', key: 'startTime', width: 170, render: (row) => formatTime(row.startTime) },
  { title: '结束时间', key: 'endTime', width: 170, render: (row) => row.endTime ? formatTime(row.endTime) : '-' }
]

import { h } from 'vue'

const evidenceModalShow = ref(false)
const evidenceJson = ref('')
const loadingEvidence = ref(false)

function formatTime(t: any) {
  return t ? dayjs(t).format('YYYY-MM-DD HH:mm') : '-'
}

function getRiskTagType(level: number): any {
  const map: Record<number, string> = { 1: 'success', 2: 'warning', 3: 'error' }
  return map[level] || 'default'
}

function gotoAppeal(restrictionId: number) {
  router.push(`/driver/submit-appeal/${restrictionId}`)
}

async function loadData() {
  if (!driverId.value) return
  try {
    activeList.value = await activeRestrictions(driverId.value)
    historyList.value = await historyRestrictions(driverId.value)
  } catch (e) {
    message.warning('加载风控记录失败，正在使用示例数据...')
    activeList.value = []
    historyList.value = []
  }
}

function viewEvidence(data: string | undefined, no: string) {
  evidenceJson.value = data ? JSON.stringify(JSON.parse(data), null, 2) : '{}'
  evidenceModalShow.value = true
}

onMounted(loadData)
</script>

<style scoped>
.stats-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}
.stat-card {
  min-height: 80px;
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
.stat-threshold {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 8px;
}
</style>
