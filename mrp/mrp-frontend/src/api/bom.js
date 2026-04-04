import api from './index'

export const BOMService = {
  // Create BOM
  createBOM: (bom) => {
    return api.post('/bom', bom)
  },
  
  // Get all BOMs
  getAllBOMs: () => {
    return api.get('/bom')
  },
  
  // Get BOM by product code
  getBOMByProductCode: (productCode) => {
    return api.get(`/bom/product/${productCode}`)
  },
  
  // Update BOM
  updateBOM: (id, bom) => {
    return api.put(`/bom/${id}`, bom)
  },
  
  // Delete BOM
  deleteBOM: (id) => {
    return api.delete(`/bom/${id}`)
  }
}
