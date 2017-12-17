const vm = new Vue({
  el: '#app',
  data: {
    isLogged:true,
    login:null
  },
  mounted () {
    this.isLogged = true;
    this.login = localStorage.getItem('RegisteredEmail');
    sessionStorage.removeItem('RegisteredEmail');
  },
  methods: {
    loginUser(log,pass){
      axios.post('http://localhost:8080/api/login',{login:log,password:pass}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          this.isLogged=true;
          localStorage.setItem('token', response.data.message);
          window.location.href = 'index.html';
        }
        else{
          this.isLogged=false;

        }
      //  store.commit('LOGIN_USER')
      }).catch( error => { console.log(error); });
    },

  }
});
