<template>
  <el-dialog v-model="dialogFormMenuVisible" draggable class="dialog-title" title="设置权限" :close-on-click-modal="false">
    <el-form ref="dataForm" :model="menuTemp" label-position="left" label-width="120px">
      <el-form-item label="操作权限">
        <el-scrollbar style="height: 60vh">
          <el-tree
            ref="menuTree"
            :data="menuData"
            :props="menuTreeProps"
            :default-checked-keys="selectMenuIds"
            default-expand-all
            show-checkbox
            node-key="id"
          />
        </el-scrollbar>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogFormMenuVisible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleChangeMenus">设置权限</el-button>
    </template>
  </el-dialog>
</template>

<script>
import { fetchRoleMenu, fetchRolePermission, setMenu, setPermission } from '@/api/sys/role'

export default {
  name: 'RoleSettingForm',
  data() {
    return {
      dialogFormMenuVisible: false,
      menuData: null,
      selectCurentRoleId: '',
      selectMenuIds: [],
      type: undefined,
      loading: false,
      menuTemp: {
        menuIds: ''
      },
      menuTreeProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    getRoleMenus(roleId) {
      this.menuData = []
      this.selectMenuIds = undefined
      fetchRoleMenu(roleId).then(response => {
        this.menuData = response.data.data.menus
        this.selectMenuIds = response.data.data.selectMenuIds
      })
    },
    getRolePermission(roleId) {
      this.menuData = []
      this.selectMenuIds = undefined
      fetchRolePermission(roleId).then(response => {
        this.menuData = response.data.data.menus
        this.selectMenuIds = response.data.data.selectMenuIds
      })
    },
    toSetMenu(row, type) {
      this.selectCurentRoleId = row.id
      this.dialogFormMenuVisible = true
      this.type = type
      if (type === 1) {
        this.getRoleMenus(this.selectCurentRoleId)
      } else {
        this.getRolePermission(this.selectCurentRoleId)
      }
    },
    changeMenu() {
      this.getRoleMenus(this.selectCurentRoleId)
    },
    handleChangeMenus() {
      const checkedKeys = this.$refs.menuTree.getCheckedKeys()
      const menuIds = checkedKeys.join(',')
      const postData = { roleId: this.selectCurentRoleId, menuIds }
      const postFun = this.type !== 1 ? setPermission : setMenu
      this.loading = true
      postFun(postData).then(response => {
        this.loading = false
        const data = response.data
        if (data.code === 0) {
          this.dialogFormMenuVisible = false
          this.$message.success('设置成功')
        } else {
          this.$message.error(data.msg)
        }
      })
    }

  }
}
</script>

<style scoped>

</style>
