import request from '@/utils/request'

export function fetchNewsTypeList(data) {
  return request({
    url: '/biz/newsType/newstype/list',
    method: 'post',
    data
  })
}

export function getNewsType(id) {
  return request({
    url: '/biz/newsType/newstype/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createNewsType(data) {
  return request({
    url: '/biz/newsType/newstype/add',
    method: 'post',
    data
  })
}

export function updateNewsType(data) {
  return request({
    url: '/biz/newsType/newstype/update',
    method: 'post',
    data
  })
}

export function deleteNewsType(ids) {
  return request({
    url: '/biz/newsType/newstype/delete',
    method: 'post',
    data: { ids }
  })
}
