import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getThreshold, uploadFile, type ThresholdConfig } from '@/api/common'
import type { Driver } from '@/api/driver'

export const useAppStore = defineStore('app', () => {
  const threshold = ref<ThresholdConfig>({
    maxConsecutiveOrders: 8,
    maxOnlineHours: 10,
    consecutiveHoursThreshold: 4,
    requireRestMinutes: 30
  })

  const currentDriver = ref<Driver | null>({
    id: 1,
    driverCode: 'D001',
    driverName: '张三',
    phone: '13800138001'
  })

  const currentReviewer = ref({
    id: 1001,
    name: '安全专员001'
  })

  async function initThreshold() {
    try {
      const data = await getThreshold()
      if (data) {
        threshold.value = data
      }
    } catch (e) {
      console.error('加载阈值失败', e)
    }
  }

  function setCurrentDriver(driver: Driver) {
    currentDriver.value = driver
  }

  async function handleUpload(file: File): Promise<string> {
    const data = await uploadFile(file)
    return data?.url || ''
  }

  return {
    threshold,
    currentDriver,
    currentReviewer,
    initThreshold,
    setCurrentDriver,
    handleUpload
  }
})
