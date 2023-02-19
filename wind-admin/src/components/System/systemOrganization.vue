<template>
  <el-cascader
    v-model="organizationIds"
    :options="treeList"
    :props="treeProps"
    :show-all-levels="false"
    label="name"
    change-on-select
    :style="{width: width}"
    :clearable="clearable"
    @change="select"
  />
</template>

<script>
import { fetchOrganizationList } from '@/api/sys/organization'
export default {
  name: 'SystemOrganization',
  props: {
    modelValue: {
      type: String,
      default: undefined
    },
    width: {
      type: String,
      default: '100%'
    },
    clearable: {
      type: Boolean,
      default: true
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      treeList: [],
      treeProps: {
        emitPath: false,
        checkStrictly: true,
        value: 'id',
        label: 'name'
      },
      organizationIds: undefined,
      selectId: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.organizationIds = val
      }
    }
  },
  created() {
    this.getTreeList()
    // this.organizationIds = this.value
  },
  methods: {
    getTreeList() {
      fetchOrganizationList(this.listQuery).then(response => {
        this.treeList = response.data.data
      })
    },
    getOrgId(treeList, id) {
      for (let i = 0; i < treeList.length; i++) {
        const item = treeList[i]
        if (item.id === id) {
          if (item.parentIds) {
            return item.parentIds + item.id
          } else {
            return item.id
          }
        }
        if (item.children !== undefined && item.children.length !== 0) {
          const ids = this.getOrgId(item.children, id)
          if (ids !== '') {
            return ids
          }
        }
      }
      return ''
    },
    select(val) {
      // console.log('val', val)
      this.selectId = val
      // console.log('this.selectId', this.selectId)
      this.$emit('update:modelValue', this.selectId)
    }
  }
}
</script>

<style scoped>

</style>
