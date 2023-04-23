<template>
  <el-cascader
    v-model="ids"
    :options="treeList"
    :props="treeProps"
    :show-all-levels="false"
    label="name"
    change-on-select
    :style="{width: width}"
    @change="select"
  />
</template>

<script>
export default {
  name: 'CascaderList',
  props: {
    modelValue: {
      type: String,
      default: undefined
    },
    list: {
      type: Array,
      default() {
        return []
      }
    },
    width: {
      type: String,
      default: '100%'
    },
    props: {
      type: Object,
      default() {
        return { value: 'id', label: 'name' }
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      treeList: [],
      treeProps: {
        emitPath: false,
        checkStrictly: true,
        value: this.props.value,
        label: this.props.label
      },
      ids: undefined
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.ids = this.getIds(this.treeList, val)
      }
    },
    list: {
      immediate: true,
      handler(val) {
        this.treeList = val
      }
    }
  },
  methods: {
    getIds(treeList, id) {
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
          const ids = this.getIds(item.children, id)
          if (ids !== '') {
            return ids
          }
        }
      }
      return ''
    },
    select(val) {
      this.$emit('update:modelValue', val)
    }
  }
}
</script>

<style scoped>

</style>
