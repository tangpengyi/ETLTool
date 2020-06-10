import vue from 'vue'
import Router from 'vue-router'
import Index from '../views/index'
import HttpTest from '../views/protocol/httpTest'
import NotFound from '../views/error/NotFound'
import Soap from '../views/protocol/soapPage'
import Socket from '../views/protocol/socket'

vue.use(Router);

export default new Router({
  mode:"history",
  routes:[
    {
      path:'/', name:'toIndex', component:Index,
      children:[{
          path:'/protocol/http',name:'toHttpTest',component:HttpTest
      },{
        path:'/protocol/soap',name:'toSoap',component:Soap
      },{
        path:'/protocol/Socket',name:'toSocket',component:Socket
      }]
    },{
      path:'*',
      component:NotFound
    }
  ]
});
