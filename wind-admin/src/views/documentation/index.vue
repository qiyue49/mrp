<template>
  <el-card class="el-card">
    <div class="app-container documentation-container">
      <h1>仓颉的特色功能</h1>
      <p>仓颉并不是针对单个框架的代码生成器，他是一个平台，支持多种开发框架，并且还在不断添加新的框架。</p>
      <a href="http://www.sunseagear.com" target="_blank">详细文档请参考http://www.sunseagear.com</a>
      <br />
      <br />
      <p>仓颉支持所见即所得的建模方式，可以以拖拽组件方式生成表单结构。</p>
      <img src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/formDesign.png" alt="表单设计" />

      <br />
      <p>仓颉支持很多组件，除了一般的文本框，下拉选择，富文本等常见组件，还包括百度地图，多图片上传，用户选择，部门选择等复杂组件，几乎覆盖了全部的常用业务。</p>

      <img width="30%" src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/baidupoint.png" alt="地图打点" />
      <img width="30%" src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/baiduline.png" alt="地图划线" />
      <img width="30%" src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/baidumaprect.png" alt="地图画框" />
      <img width="30%" src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/chooseUser.png" alt="地图画框" />
      <img width="30%" src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/uploadimage.png" alt="地图画框" />
      <br />

      <p>仓颉支持多表联查的代码生成，例如可以通过表中的userId来关联用户表查询出用户的信息，并且直接展示在页面上。</p>

      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/tableJoin.PNG" /> <br />
      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/userorg.png" /> <br />
      <br />

      <p>仓颉不但支持单数据表的模块生成还支持多数据表的组合模块，例如二级级联表，左树右表的结构。</p>

      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/twotable.png" /> <br />
      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/user.png" /> <br />
      <br />

      <p>仓颉不但支持服务器端的框架，还支持移动端的框架，并且针对同一模块，可以同时生成移动端和服务器端的代码。并且生成以后的代码直接能够实现数据互通。</p>
      <p style="font-size: 16px">服务器端页面</p>
      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/eventpc.png" /> <br />
      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/eventdetailpc.png" /> <br />
      <p style="font-size: 16px">对应的移动端页面</p>
      <img src="http://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/eventmobile.png" />
      <img src="https://feilian.oss-cn-beijing.aliyuncs.com/cangjie/components2/eventdetailmobile.png" />
    </div>
  </el-card>
</template>

<script>
export default {
  name: 'Documentation',
  data() {
    return {
    }
  }
}
</script>

<style lang="scss" scoped>
.documentation-container {
  margin: 50px;
  .document-btn {
    float: left;
    margin-left: 50px;
    display: block;
    cursor: pointer;
    background: black;
    color: white;
    height: 60px;
    width: 200px;
    line-height: 60px;
    font-size: 20px;
    text-align: center;
  }
  img {
    border: 1px solid #a9a9a9;
    margin-left: 12px;
  }
  p{
    font-size: 18px;
  }
}
</style>
