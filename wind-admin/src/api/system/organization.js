import request from '@/utils/request'

// 组织结构
export function fetchOrganizationList(data) {
  return request({
    url: `/json/organization/list`,
    method: 'post',
    data
  })
}

export function getOrganization(id) {
  return request({
    url: `/json/organization/detail/${id}`,
    method: 'get'
  })
}
