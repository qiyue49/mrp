import request from '@/utils/request'

export function fetchCompanyList(data) {
  return request({
    url: '/biz/company/company/list',
    method: 'post',
    data
  })
}

export function getCompany(id) {
  return request({
    url: '/biz/company/company/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createCompany(data) {
  return request({
    url: '/biz/company/company/add',
    method: 'post',
    data
  })
}

export function updateCompany(data) {
  return request({
    url: '/biz/company/company/update',
    method: 'post',
    data
  })
}

export function deleteCompany(ids) {
  return request({
    url: '/biz/company/company/delete',
    method: 'post',
    data: { ids }
  })
}
