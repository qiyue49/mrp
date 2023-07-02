import request from '@/utils/request'

export function fetchDataRuleList(data) {
  return request({
    url: '/sys/datarule/list',
    method: 'post',
    data
  })
}

export function getDataRule(id) {
  return request({
    url: '/sys/datarule/detail/' + id,
    method: 'get'
  })
}

export function createDataRule(data) {
  return request({
    url: '/sys/datarule/add',
    method: 'post',
    data
  })
}

export function updateDataRule(data) {
  return request({
    url: '/sys/datarule/update',
    method: 'post',
    data
  })
}

export function deleteDataRule(ids) {
  return request({
    url: '/sys/datarule/delete',
    method: 'post',
    data: { ids }
  })
}
