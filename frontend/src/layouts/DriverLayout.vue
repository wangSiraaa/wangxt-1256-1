<template>
  <n-layout style="height: 100vh">
    <n-layout-sider bordered width="220" :collapsed-width="64" :show-trigger="true">
      <div class="logo">
        <h2>司机端</h2>
        <span>疲劳驾驶申诉</span>
      </div>
      <n-menu
        :value="activeMenu"
        :options="menuOptions"
        @update:value="handleMenuChange"
      />
      <div class="role-switch">
        <n-button size="small" block type="primary" ghost @click="switchToPlatform">
          切换到平台管理
        </n-button>
      </div>
    </n-layout-sider>
    <n-layout>
      <n-layout-header bordered style="height: 64px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px;">
        <div>
          <n-text depth="3">{{ pageTitle }}</n-text>
        </div>
        <div style="display: flex; align-items: center; gap: 16px;">
          <n-select
            v-model:value="selectedDriverId"
            :options="driverOptions"
            label-field="driverName"
            value-field="id"
            style="width: 220px;"
            :render-label="renderDriverLabel"
            @update:value="changeDriver"
          />
        </div>
      </n-layout-header>
      <n-layout-content content-style="padding: 0; height: calc(100vh - 64px); overflow: auto;">
        <div class="page-wrapper">
          <router-view v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, type SelectRenderLabel } from 'vue'
import { useRoute, useRouter, type MenuOption } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { useMessage } from 'naive-ui'
import { listDrivers, type Driver } from '@/api/driver'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const appStore = useAppStore()
const { currentDriver } = storeToRefs(appStore)

const menuOptions: MenuOption[] = [
  { label: '我的限制', key: '/driver/my-restrictions', icon: () => h('span', '🚫') },
  { label: '我的申诉', key: '/driver/my-appeals', icon: () => h('span', '📝') }
]

import { h } from 'vue'

const activeMenu = computed(() => {
  if (route.path.startsWith('/driver/submit-appeal')) {
    return '/driver/my-restrictions'
  }
  return route.path
})

const pageTitle = computed(() => {
  const metaTitle = (route.meta?.title as string) || ''
  return metaTitle
})

const drivers = ref<Driver[]>([])
const driverOptions = computed(() => drivers.value)
const selectedDriverId = ref<number | null>(currentDriver.value?.id || null)

const renderDriverLabel: SelectRenderLabel = (option) => {
  const d = option as unknown as Driver
  return h(
    'div',
    { style: { display: 'flex', justifyContent: 'space-between', width: '100%' } },
    [
      h('span', { style: { fontWeight: 500 } }, d.driverName),
      h('span', { style: { fontSize: '12px', color: '#999' } }, d.driverCode + ' ' + d.phone)
    ]
  )
}

async function changeDriver(id: number) {
  const driver = drivers.value.find(d => d.id === id)
  if (driver) {
    appStore.setCurrentDriver(driver)
    message.success('已切换司机：' + driver.driverName)
  }
}

function handleMenuChange(key: string) {
  router.push(key)
}

function switchToPlatform() {
  router.push('/platform/hours-import')
  message.success('已切换到平台管理端')
}

onMounted(async () => {
  try {
    drivers.value = await listDrivers()
    if (!selectedDriverId.value && drivers.value.length > 0) {
      changeDriver(drivers.value[0].id)
    }
  } catch (e) {
    console.error(e)
  }
})
</script>

<style scoped>
.logo {
  padding: 20px 16px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
}
.logo h2 {
  font-size: 18px;
  margin: 0 0 4px 0;
  color: #2080f0;
  font-weight: 700;
}
.logo span {
  font-size: 12px;
  color: #999;
}
.role-switch {
  padding: 16px;
  border-top: 1px solid #f0f0f0;
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
