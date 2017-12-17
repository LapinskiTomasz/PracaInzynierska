const vm = new Vue({
  el: '#app',
  data: {
    isRegistered:true
  },
  mounted () {
    this.isRegistered = true;
  },
  methods: {
    registerUser(log,pass,mail){
      axios.post('http://localhost:8080/api/register',{login:log,password:pass,email:mail}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          this.isLogged=true;
          localStorage.setItem('RegisteredEmail', mail);
          window.location.href = 'login.html';
        } else{
          this.isRegistered=false;

        }

      }).catch( error => { console.log(error); });
    },

  }
});
