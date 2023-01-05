import request from '@/utils/request'

export function fetchEventList(data) {
  return request({
    url: '/biz/event/event/list',
    method: 'post',
    data
  })
}

export function getEvent(id) {
  return request({
    url: '/biz/event/event/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createEvent(data) {
  return request({
    url: '/biz/event/event/add',
    method: 'post',
    data
  })
}

export function updateEvent(data) {
  return request({
    url: '/biz/event/event/update',
    method: 'post',
    data
  })
}

export function deleteEvent(ids) {
  return request({
    url: '/biz/event/event/delete',
    method: 'post',
    data: { ids }
  })
}
