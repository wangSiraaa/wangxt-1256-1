import request from './common'
import type { PageResult, PageQuery } from './common'

export interface SafetyReviewDTO {
  appealId: number
  restrictionId: number
  reviewResult: number
  reviewRemark: string
  reviewerId: number
  reviewerName: string
  verifyEvidence?: string
  restVerifiedMinutes?: number
  keepEvidence?: number
}

export interface SafetyReviewVO {
  id: number
  appealId: number
  restrictionId: number
  driverId: number
  reviewNo: string
  reviewerId: number
  reviewerName: string
  reviewResult: number
  reviewResultDesc: string
  reviewRemark: string
  verifyEvidence?: string
  restVerifiedMinutes?: number
  reviewTime: string
  keepEvidence: number
  createTime: string
  driverCode?: string
  driverName?: string
  appealNo?: string
  restrictionNo?: string
}

export function doReview(dto: SafetyReviewDTO) {
  return request<SafetyReviewDTO, SafetyReviewVO>('/api/review/do', 'post', dto)
}

export function reviewPage(query: PageQuery) {
  return request<PageQuery, PageResult<SafetyReviewVO>>('/api/review/page', 'post', query)
}

export function reviewDetail(id: number) {
  return request<any, SafetyReviewVO>(`/api/review/${id}`, 'get')
}

export function getReviewByAppeal(appealId: number) {
  return request<any, SafetyReviewVO>(`/api/review/appeal/${appealId}`, 'get')
}
