<template>
  <el-select v-model="attrValue" :filterable="filterable" :placeholder="placeholder" :style="{width: width}" @change="change">
    <el-option
      v-for="item in attrList"
      :key="item[props.value] + 'scopeType'"
      :label="item[props.label]"
      :value="item[props.value]"
    />
  </el-select>
</template>

<script>
export default {
  name: 'SelectList',
  props: {
    modelValue: {
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
    width: {
      type: String,
      default: '100%'
    },
    props: {
      type: Object,
      default() {
        return { value: 'value', label: 'label' }
      }
    }
  },
  emits: ['update:modelValue'],
  data() {
    return {
      attrValue: undefined,
      attrList: undefined
    }
  },
  watch: {
    modelValue: {
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
      this.$emit('update:modelValue', this.attrValue)
    }

  }

}
</script>

<style scoped>

</style>
