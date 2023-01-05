import request from '@/utils/request'

export function fetchProductList(data) {
  return request({
    url: '/biz/company/product/list',
    method: 'post',
    data
  })
}

export function getProduct(id) {
  return request({
    url: '/biz/company/product/detail/' + id,
    method: 'get',
    params: { id }
  })
}

export function createProduct(data) {
  return request({
    url: '/biz/company/product/add',
    method: 'post',
    data
  })
}

export function updateProduct(data) {
  return request({
    url: '/biz/company/product/update',
    method: 'post',
    data
  })
}

export function deleteProduct(ids) {
  return request({
    url: '/biz/company/product/delete',
    method: 'post',
    data: { ids }
  })
}
