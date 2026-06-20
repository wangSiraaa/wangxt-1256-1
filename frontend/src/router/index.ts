import type { App } from 'vue'
import { createRouter, createWebHashHistory, Router, RouteRecordRaw } from 'vue-router'
import { useMessage } from 'naive-ui'
import { setupMessage } from '@/utils/request'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/platform'
  },
  {
    path: '/platform',
    name: 'Platform',
    component: () => import('@/layouts/MainLayout.vue'),
    redirect: '/platform/hours-import',
    children: [
      {
        path: 'hours-import',
        name: 'HoursImport',
        component: () => import('@/views/platform/HoursImport.vue'),
        meta: { title: '时长导入', icon: 'cloud-upload' }
      },
      {
        path: 'restriction-list',
        name: 'RestrictionList',
        component: () => import('@/views/platform/RestrictionList.vue'),
        meta: { title: '风控限制列表', icon: 'shield-alert' }
      },
      {
        path: 'appeal-audit',
        name: 'AppealAudit',
        component: () => import('@/views/platform/AppealAudit.vue'),
        meta: { title: '申诉材料审核', icon: 'file-check' }
      },
      {
        path: 'safety-review',
        name: 'SafetyReview',
        component: () => import('@/views/platform/SafetyReview.vue'),
        meta: { title: '安全复核', icon: 'shield-checkmark' }
      },
      {
        path: 'review-list',
        name: 'ReviewList',
        component: () => import('@/views/platform/ReviewList.vue'),
        meta: { title: '复核记录', icon: 'document-text' }
      },
      {
        path: 'driver',
        name: 'DriverManagement',
        component: () => import('@/views/platform/DriverManagement.vue'),
        meta: { title: '司机管理', icon: 'people' }
      }
    ]
  },
  {
    path: '/driver',
    name: 'Driver',
    component: () => import('@/layouts/DriverLayout.vue'),
    redirect: '/driver/my-restrictions',
    children: [
      {
        path: 'my-restrictions',
        name: 'MyRestrictions',
        component: () => import('@/views/driver/MyRestrictions.vue'),
        meta: { title: '我的限制' }
      },
      {
        path: 'submit-appeal/:restrictionId',
        name: 'SubmitAppeal',
        component: () => import('@/views/driver/SubmitAppeal.vue'),
        meta: { title: '提交申诉' }
      },
      {
        path: 'my-appeals',
        name: 'MyAppeals',
        component: () => import('@/views/driver/MyAppeals.vue'),
        meta: { title: '我的申诉' }
      }
    ]
  }
]

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes
})

export function setup(app: App) {
  router.isReady().then(() => {
    const msg = useMessage()
    setupMessage(msg)
  })
  app.use(router)
}

export default router
