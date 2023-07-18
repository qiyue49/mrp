<template>
  <el-autocomplete
    v-model="attrValue"
    :fetch-suggestions="querySearch"
    :placeholder="placeholder"
    style="width: 100%"
    @select="handleSelect"
  />
</template>

<script>
export default {
  name: 'AutocompleteList',
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请选择'
    },
    list: {
      type: Array,
      default() {
        return []
      }
    },
    props: {
      type: Object,
      default() {
        return { value: 'value', id: 'id' }
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      attrValue: undefined,
      attrList: []
    }
  },
  watch: {
    modelValue: {
      immediate: true,
      handler(val) {
        this.refreshListValue()
      }
    },
    list: {
      immediate: true,
      handler(val) {
        this.attrList = val
        this.attrList.forEach(item => {
          item.value = item[this.props.value]
        })
        this.refreshListValue()
      }
    }

  },
  methods: {
    refreshListValue() {
      this.attrList.forEach(item => {
        if (this.modelValue === item[this.props.id]) {
          this.attrValue = item[this.props.value]
        }
      })
    },
    querySearch(queryString, cb) {
      const list = this.attrList
      const results = queryString ? list.filter(this.createFilter(queryString)) : list
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (item) => {
        return item.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
      }
    },
    handleSelect(val) {
      this.$emit('update:modelValue', val[this.props.id])
    }

  }

}
</script>

<style scoped>

</style>
