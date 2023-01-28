import request from '@/utils/request'

export function fileUpload(data) {
  return request({
    url: '/json/oss/uploadBase',
    method: 'post',
    data
  })
}
