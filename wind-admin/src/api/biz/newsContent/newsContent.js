import request from '@/utils/request'

export function fetchNewsContentList(data) {
  return request({
    url: '/biz/newsContent/newscontent/list',
    method: 'post',
    data
  })
}

export function getNewsContent(id) {
  return request({
    url: '/biz/newsContent/newscontent/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createNewsContent(data) {
  return request({
    url: '/biz/newsContent/newscontent/add',
    method: 'post',
    data
  })
}

export function updateNewsContent(data) {
  return request({
    url: '/biz/newsContent/newscontent/update',
    method: 'post',
    data
  })
}

export function deleteNewsContent(ids) {
  return request({
    url: '/biz/newsContent/newscontent/delete',
    method: 'post',
    data: { ids }
  })
}
// 获取所有新闻类型内容
export function getNewsContentType() {
  return request({
    url: 'biz/newsType/newstype/select',
    method: 'get'
  })
}
