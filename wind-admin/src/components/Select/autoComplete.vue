<template>
  <el-autocomplete
    v-model="attrValue"
    :fetch-suggestions="querySearch"
    class="inline-input"
    :placeholder="placeholder"
    @select="handleSelect"
  />
</template>

<script>
export default {
  name: 'AutoComplete',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
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
        return { value: 'value' }
      }
    }
  },
  data() {
    return {
      attrValue: undefined,
      attrList: undefined
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        this.attrValue = val
      }
    },
    list: {
      immediate: true,
      handler(val) {
        this.attrList = val
        this.attrList.forEach(item => {
          item.value = item[this.props.value]
        })
      }
    }

  },
  methods: {
    querySearch(queryString, cb) {
      var list = this.attrList
      var results = queryString ? list.filter(this.createFilter(queryString)) : list
      // 调用 callback 返回建议列表的数据
      cb(results)
    },
    createFilter(queryString) {
      return (item) => {
        return item.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1
      }
    },
    handleSelect(val) {
      this.$emit('input', this.attrValue)
    }

  }

}
</script>

<style scoped>

</style>
