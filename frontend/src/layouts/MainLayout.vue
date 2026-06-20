<template>
  <n-layout style="height: 100vh">
    <n-layout-sider bordered width="240" :collapsed-width="64" :show-trigger="true">
      <div class="logo">
        <h2>风控系统</h2>
        <span>平台管理端</span>
      </div>
      <n-menu
        :value="activeMenu"
        :options="menuOptions"
        @update:value="handleMenuChange"
      />
      <div class="role-switch">
        <n-button size="small" block type="primary" ghost @click="switchToDriver">
          切换到司机端
        </n-button>
      </div>
    </n-layout-sider>
    <n-layout>
      <n-layout-header bordered style="height: 64px; display: flex; align-items: center; justify-content: space-between; padding: 0 24px;">
        <div>
          <n-text depth="3">{{ pageTitle }}</n-text>
        </div>
        <div style="display: flex; align-items: center; gap: 16px;">
          <n-space>
            <n-tag>
              <template #avatar>
                <n-avatar size="small" round>
                  {{ reviewer.name.slice(-2) }}
                </n-avatar>
              </template>
              {{ reviewer.name }}
            </n-tag>
            <n-tag type="success">阈值：连续{{ threshold.maxConsecutiveOrders }}单 / 在线{{ threshold.maxOnlineHours }}时 / 连续{{ threshold.consecutiveHoursThreshold }}时</n-tag>
          </n-space>
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
import { computed, ref } from 'vue'
import { useRoute, useRouter, type MenuOption } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { storeToRefs } from 'pinia'
import { useMessage } from 'naive-ui'

const router = useRouter()
const route = useRoute()
const message = useMessage()
const appStore = useAppStore()
const { threshold, currentReviewer } = storeToRefs(appStore)
const reviewer = currentReviewer.value

const menuOptions: MenuOption[] = [
  { label: '时长导入', key: '/platform/hours-import', icon: () => h('span', '📥') },
  { label: '风控限制列表', key: '/platform/restriction-list', icon: () => h('span', '🚫') },
  { label: '申诉材料审核', key: '/platform/appeal-audit', icon: () => h('span', '📋') },
  { label: '安全复核', key: '/platform/safety-review', icon: () => h('span', '🛡️') },
  { label: '复核记录', key: '/platform/review-list', icon: () => h('span', '📑') },
  { label: '司机管理', key: '/platform/driver', icon: () => h('span', '👥') }
]

import { h } from 'vue'

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const metaTitle = (route.meta?.title as string) || ''
  return metaTitle
})

function handleMenuChange(key: string) {
  router.push(key)
}

function switchToDriver() {
  router.push('/driver/my-restrictions')
  message.success('已切换到司机端视图')
}
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
  color: #18a058;
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
