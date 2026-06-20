import request from './common'

export interface Driver {
  id: number
  driverCode: string
  driverName: string
  phone: string
  licenseNo?: string
  status?: number
}

export function listDrivers() {
  return request<any, Driver[]>('/api/driver/list', 'get')
}

export function searchDrivers(keyword: string) {
  return request<any, Driver[]>(`/api/driver/search?keyword=${keyword || ''}`, 'get')
}

export function getDriver(id: number) {
  return request<any, Driver>(`/api/driver/${id}`, 'get')
}

export function getDriverByCode(driverCode: string) {
  return request<any, Driver>(`/api/driver/code/${driverCode}`, 'get')
}
