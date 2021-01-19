<template>
  <div id="app">
    <img id="logo" src="./assets/logo.jpg">

    <div>
      <el-link href="http://172.25.104.57:81/download/share/" type="primary" target="_blank">前往文件目录</el-link>
    </div>

    <el-form ref="dataForm" :model="dataForm" label-width="80px" >
      <el-form-item label="志文电脑IP" label-width="110px">
        <el-input v-model="dataForm.ip"></el-input>
      </el-form-item>
      <el-form-item label="上传文件" prop="file">
        <el-upload
          class="upload-demo"
          drag
          :action=uploadUrl
          :show-file-list="true"
          :file-list="fileList"
          list-type="picture"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError"
          multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip"></div>
        </el-upload>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      uploadUrl: 'http://172.25.104.57:8866/files/uploadToNginx',
      fileList: [
        {name: 'food.jpeg', url: 'http://172.25.104.57:81/download/pic/1.png'},
        {name: 'food2.jpeg', url: 'http://172.25.104.57:81/download/pic/2.jpg'}
      ],
      dataForm: {
        ip: '172.25.104.57'
      }
    }
  },
  methods: {
    handleUploadSuccess: function (response) {
      console.log(response)
      this.$message({
        message: '文件上传成功',
        type: 'success'
      })
    },
    handleUploadError: function (response) {
      console.log(response)
      this.$message.error({
        message: '文件上传失败'
      })
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  margin: 0 auto;
  width: 500px;
}
#logo {
  width : 480px;
}
</style>
