<template>
  <el-select v-model="attrValue" :filterable="filterable" :placeholder="placeholder" @change="change">
    <el-option
      v-for="item in attrList"
      :key="item.label + 'scopeType'"
      :label="item[props.label]"
      :value="item[props.value]"
    />
  </el-select>
</template>

<script>
export default {
  name: 'SelectList',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: ''
    },
    filterable: {
      type: Boolean,
      default: false
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
        return { value: 'value', label: 'label' }
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
      }
    }
  },
  methods: {
    change(val) {
      this.$emit('input', this.attrValue)
    }

  }

}
</script>

<style scoped>

</style>
