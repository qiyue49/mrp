import api from './index'

const materialApi = {
  // Get materials with pagination
  getMaterials: (params) => {
    return api.get('/mrp/material/list', { params })
  },
  
  // Get material by ID
  getMaterial: (id) => {
    return api.get(`/mrp/material/${id}`)
  },
  
  // Create new material
  createMaterial: (material) => {
    return api.post('/mrp/material', material)
  },
  
  // Update material
  updateMaterial: (id, material) => {
    return api.put(`/mrp/material/${id}`, material)
  },
  
  // Delete material
  deleteMaterial: (id) => {
    return api.delete(`/mrp/material/${id}`)
  }
}

export default materialApi
