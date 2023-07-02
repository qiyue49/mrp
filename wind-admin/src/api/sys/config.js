import request from '@/utils/request'

export function fetchConfigList(data) {
  return request({
    url: '/sys/config/list',
    method: 'post',
    data
  })
}

export function getConfig(id) {
  return request({
    url: '/sys/config/detail/' + id,
    method: 'get'
  })
}

export function createConfig(data) {
  return request({
    url: '/sys/config/add',
    method: 'post',
    data
  })
}

export function updateConfig(data) {
  return request({
    url: '/sys/config/update',
    method: 'post',
    data
  })
}

export function deleteConfig(ids) {
  return request({
    url: '/sys/config/delete',
    method: 'post',
    data: { ids }
  })
}

export function getUserConfigList() {
  return request({
    url: '/sys/config/config',
    method: 'get'
  })
}

