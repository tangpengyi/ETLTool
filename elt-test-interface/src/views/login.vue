<template>
  <div class="login-context">
    <h3 class="titlestyle">ETL接口测试</h3>
    <el-form :model="loginForm" :rules="relus" class="loginstype" ref="loginForm" label-position="left">
      <p class="login-box-msg">欢迎登陆</p>
      <el-form-item prop="username">
        <el-input suffix-icon="el-icon-user-solid" v-model="loginForm.username" type="text" placeholder="请输入账号"></el-input>
      </el-form-item>
      <el-form-item prop="userPwd">
        <el-input suffix-icon="el-icon-lock" v-model="loginForm.userPwd" type="password" placeholder="请输入密码"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="loginSubmit('loginForm')">登陆</el-button>
        <el-button @click="resetForm('loginForm')">重置</el-button>
      </el-form-item>

    </el-form>
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        loginForm: {
          username: '',
          userPwd: ''
        },
        relus: {
          username: [
            {required: true, message: '请输入账号', trigger: 'blur'},
            {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
          ],
          userPwd: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      loginSubmit(loginform) {

        this.$refs[loginform].validate((valid) => {
          if (valid) {
            this.axios.post('/api/user/login',
              qs.stringify({ 'user': this.loginForm },{page:1},{row:10})
            )
              .then((response) => {
                const result = response.data;
                // 登陆成功
                if (result.code == 1) {
                  sessionStorage.setItem('isLogin', 'true');
                  sessionStorage.setItem('isRefresh', 0);
                  this.$router.push({name: 'toHttpTest'});
                } else if (result.code == 0) {
                  this.$message.error(result.msg);
                }
              })
              .catch(function (error) {
                this.$message.error("失败");
                console.log(error);
              })
              .then(function () {
                // always executed
              });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      addParasReq(date){
        return request({
          url:"",
          method:'post'
        })
      }
    }
  }
</script>


<style lang="scss">

  .titlestyle {
    text-align: center;
    margin-top: 100px;
  }

  /*全频显示*/
  .login-context {
    height: 100%;
    width: 100%;
    position: absolute;
    top: 0;
    bottom: 0;
    background: #D2D6DE;
    z-index: 0;
    overflow: hidden;
    word-break: break-all;
  }

  .loginstype {
    background: white;
    width: 290px;
    margin: auto;
    border: 1px solid #DCDFE6;
    padding-left: 26px;
    padding-right: 26px;
    border-radius: 4px;
    box-shadow: 0 0 25px #DCDFE6;
  }

  .el-input {
    height: 47px;
    width: 270px;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      height: 36px;
      width: 250px;
    }
  }



  .login-box-msg {
    margin-top: 20px;
    color: #889aa4;
    text-align: center;
    font-size: small;
  }
</style>


