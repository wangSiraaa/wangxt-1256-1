<template>
  <div>
    <div class="card-wrapper">
      <n-tabs v-model:value="tab" type="line">
        <n-tab-pane name="single" tab="📥 单条导入">
          <n-card style="max-width: 800px; margin-top: 16px;">
            <n-form :model="singleForm" :cols="2" label-placement="top">
              <n-grid :cols="2" :x-gap="16">
                <n-form-item label="选择司机" path="driverId" :span="2">
                  <n-select
                    v-model:value="singleForm.driverId"
                    :options="driverOptions"
                    :on-search="onDriverSearch"
                    :loading="driverLoading"
                    filterable
                    placeholder="搜索司机（工号/姓名/手机号）"
                    style="width: 100%;"
                    :render-label="renderDriverLabel"
                  />
                </n-form-item>
                <n-form-item label="工作日期" path="workDate" :span="1">
                  <n-date-picker
                    v-model:value="singleForm.workDate"
                    type="date"
                    value-format="yyyy-MM-dd"
                    style="width: 100%;"
                  />
                </n-form-item>
                <n-form-item label="接单数量" path="orderCount" :span="1">
                  <n-input-number v-model:value="singleForm.orderCount" placeholder="0" :min="0" style="width: 100%;" />
                </n-form-item>
                <n-form-item label="上线时间" path="onlineStart" :span="1">
                  <n-date-picker
                    v-model:value="singleForm.onlineStart"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%;"
                    @update:value="calcMinutes"
                  />
                </n-form-item>
                <n-form-item label="下线时间" path="onlineEnd" :span="1">
                  <n-date-picker
                    v-model:value="singleForm.onlineEnd"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 100%;"
                    @update:value="calcMinutes"
                  />
                </n-form-item>
                <n-form-item label="在线时长(分钟)" path="onlineMinutes" :span="1">
                  <n-input-number v-model:value="singleForm.onlineMinutes" placeholder="自动计算" :min="0" style="width: 100%;" />
                </n-form-item>
                <n-form-item label="实际工作(分钟)" path="workMinutes" :span="1">
                  <n-input-number v-model:value="singleForm.workMinutes" placeholder="默认=在线时长" :min="0" style="width: 100%;" />
                </n-form-item>
                <n-form-item label="休息时长(分钟)" path="restMinutes" :span="1">
                  <n-input-number v-model:value="singleForm.restMinutes" placeholder="0" :min="0" style="width: 100%;" />
                </n-form-item>
              </n-grid>
              <div style="text-align: right; margin-top: 16px;">
                <n-button @click="resetForm">重置</n-button>
                <n-button type="primary" :loading="importing" @click="doImport" style="margin-left: 12px;">
                  导入并触发风控判定
                </n-button>
              </div>
            </n-form>
          </n-card>
        </n-tab-pane>
        <n-tab-pane name="batch" tab="⚡️ 一键批量模拟（演示）">
          <n-card style="margin-top: 16px;">
            <n-space vertical :size="16">
              <n-alert type="info" :show-icon="true">
                <template #title>批量导入演示说明</template>
                此功能用于快速测试风控判定逻辑。选择司机、天数和强度后，系统会自动生成连续的时长记录并立即触发风控判定。
              </n-alert>
              <n-grid :cols="3" :x-gap="16">
                <n-form-item label="选择司机">
                  <n-select
                    v-model:value="batchForm.driverId"
                    :options="driverOptions"
                    filterable
                    :render-label="renderDriverLabel"
                    style="width: 100%;"
                  />
                </n-form-item>
                <n-form-item label="连续天数">
                  <n-input-number v-model:value="batchForm.days" :min="1" :max="30" style="width: 100%;" />
                </n-form-item>
                <n-form-item label="强度（单天接单量）">
                  <n-select v-model:value="batchForm.intensity" :options="intensityOptions" style="width: 100%;" />
                </n-form-item>
              </n-grid>
              <div>
                <n-tag size="small" v-if="batchForm.intensity === 'low'">强度低：每天4单×6小时</n-tag>
                <n-tag size="small" type="warning" v-else-if="batchForm.intensity === 'medium'">强度中：每天6单×9小时</n-tag>
                <n-tag size="small" type="error" v-else>强度高：每天10单×12小时（必触发）</n-tag>
              </div>
              <div style="text-align: right;">
                <n-button type="primary" :loading="importing" @click="doBatchImport">
                  批量导入
                </n-button>
              </div>
            </n-space>
          </n-card>
        </n-tab-pane>
        <n-tab-pane name="history" tab="📊 导入历史">
          <n-card style="margin-top: 16px;">
            <n-space vertical :size="12">
              <div>
                <n-select
                  v-model:value="historyDriverId"
                  :options="driverOptions"
                  :render-label="renderDriverLabel"
                  clearable
                  placeholder="选择司机查看历史"
                  style="width: 260px;"
                />
                <n-button style="margin-left: 12px;" @click="loadHistory">查询</n-button>
              </div>
              <n-data-table
                :columns="historyColumns"
                :data="historyList"
                :loading="historyLoading"
                :bordered="true"
                size="small"
              />
            </n-space>
          </n-card>
        </n-tab-pane>
      </n-tabs>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, h, onMounted, reactive, ref } from 'vue'
import dayjs from 'dayjs'
import {
  importHours,
  batchImportHours,
  listHours,
  type DriverOnlineHours,
  type OnlineHoursImportDTO
} from '@/api/risk'
import { listDrivers, searchDrivers, type Driver } from '@/api/driver'
import { useMessage, type SelectRenderLabel, type DataTableColumns } from 'naive-ui'

const message = useMessage()

const tab = ref('single')
const driverLoading = ref(false)
const importing = ref(false)
const drivers = ref<Driver[]>([])
const driverOptions = computed(() =>
  drivers.value.map((d) => ({ ...d, label: `${d.driverName} ${d.driverCode}`, value: d.id }))

const renderDriverLabel: SelectRenderLabel = (option) => {
  const d = option as unknown as Driver
  return h(
    'div',
    { style: { display: 'flex', justifyContent: 'space-between', width: '100%' },
    [
      h('span', { style: { fontWeight: 500 } }, `${d.driverName + '(' + d.driverCode + ')' }),
      h('span', { style: { fontSize: '12px', color: '#999' } }, d.phone)
    ]
  )
}

const singleForm = reactive<OnlineHoursImportDTO>({
  driverId: undefined as any,
  workDate: dayjs().format('YYYY-MM-DD'),
  onlineStart: '',
  onlineEnd: '',
  onlineMinutes: undefined,
  workMinutes: undefined,
  restMinutes: 0,
  orderCount: 0
})

const batchForm = reactive({
  driverId: undefined as any,
  days: 3,
  intensity: 'high'
})

const intensityOptions = [
  { label: '低强度', value: 'low' },
  { label: '中强度', value: 'medium' },
  { label: '高强度（必触发）', value: 'high' }
]

function calcMinutes() {
  if (singleForm.onlineStart && singleForm.onlineEnd) {
    const m = dayjs(singleForm.onlineEnd).diff(dayjs(singleForm.onlineStart), 'minute')
    if (m > 0) {
      singleForm.onlineMinutes = m
      if (!singleForm.workMinutes) singleForm.workMinutes = m
    }
  }
}

async function onDriverSearch(query: string) {
  if (!query) {
    drivers.value = await listDrivers()
    return
  }
  driverLoading.value = true
  try {
    drivers.value = await searchDrivers(query)
  } finally {
    driverLoading.value = false
  }
}

function resetForm() {
  singleForm.driverId = undefined as any
  singleForm.workDate = dayjs().format('YYYY-MM-DD')
  singleForm.onlineStart = ''
  singleForm.onlineEnd = ''
  singleForm.onlineMinutes = undefined
  singleForm.workMinutes = undefined
  singleForm.restMinutes = 0
  singleForm.orderCount = 0
}

async function doImport() {
  if (!singleForm.driverId) {
    message.error('请选择司机')
    return
  }
  if (!singleForm.onlineStart || !singleForm.onlineEnd) {
    message.error('请选择上线和下线时间')
    return
  }
  importing.value = true
  try {
    const res = await importHours(singleForm)
    message.success('导入成功，系统已自动触发风控判定')
    loadHistory()
  } catch (e: any) {
    message.error(e.message || '导入失败')
  } finally {
    importing.value = false
  }
}

async function doBatchImport() {
  if (!batchForm.driverId) {
    message.error('请选择司机')
    return
  }
  importing.value = true
  const list: OnlineHoursImportDTO[] = []
  const today = dayjs()
  const config = {
    low: { orders: 4, hours: 6 },
    medium: { orders: 6, hours: 9 },
    high: { orders: 10, hours: 12 }
  }[batchForm.intensity as 'low' | 'medium' | 'high']
  for (let i = batchForm.days - 1; i >= 0; i--) {
    const date = today.subtract(i, 'day')
    const start = date.hour(8).minute(0).second(0)
    const end = date.hour(8 + config.hours).minute(0).second(0)
    list.push({
      driverId: batchForm.driverId,
      workDate: date.format('YYYY-MM-DD'),
      onlineStart: start.format('YYYY-MM-DD HH:mm:ss'),
      onlineEnd: end.format('YYYY-MM-DD HH:mm:ss'),
      onlineMinutes: config.hours * 60,
      workMinutes: config.hours * 60,
      restMinutes: 0,
      orderCount: config.orders
    })
  }
  try {
    await batchImportHours(list)
    message.success(`成功导入 ${list.length} 条记录，风控判定已自动执行`)
    tab.value = 'history'
    historyDriverId.value = batchForm.driverId
    loadHistory()
  } catch (e: any) {
    message.error(e.message || '批量导入失败')
  } finally {
    importing.value = false
  }
}

const historyDriverId = ref<number | null>(null)
const historyLoading = ref(false)
const historyList = ref<DriverOnlineHours[]>([])
const historyColumns: DataTableColumns<DriverOnlineHours> = [
  { title: '日期', key: 'workDate', width: 120 },
  { title: '上线', key: 'onlineStart', width: 170 },
  { title: '下线', key: 'onlineEnd', width: 170 },
  { title: '在线(分)', key: 'onlineMinutes', width: 90 },
  { title: '工作(分)', key: 'workMinutes', width: 90 },
  { title: '休息(分)', key: 'restMinutes', width: 90 },
  { title: '接单数', key: 'orderCount', width: 90 }
]

async function loadHistory() {
  if (!historyDriverId.value) {
    message.info('请先选择司机')
    return
  }
  historyLoading.value = true
  try {
    const end = dayjs().format('YYYY-MM-DD')
    const start = dayjs().subtract(30, 'day').format('YYYY-MM-DD')
    historyList.value = await listHours(historyDriverId.value, start, end)
  } catch (e) {
    historyList.value = []
  } finally {
    historyLoading.value = false
  }
}

onMounted(async () => {
  try {
    drivers.value = await listDrivers()
    if (drivers.value.length) {
      singleForm.driverId = drivers.value[0].id
    }
  } catch (e) {}
})
</script>
