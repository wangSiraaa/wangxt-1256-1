import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'
import type { App } from 'vue'
import { useMessage } from 'naive-ui'

let message: ReturnType<typeof useMessage> | null = null

const service: AxiosInstance = axios.create({
  baseURL: '/',
  timeout: 30000
})

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    if (res.code !== 200) {
      if (message) {
        message.error(res.message || '请求失败')
      }
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res.data
  },
  (error) => {
    if (message) {
      message.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export function setupMessage(_message: ReturnType<typeof useMessage>) {
  message = _message
}

export function setupAxios(app: App) {
  app.config.globalProperties.$axios = service
}

export default service
