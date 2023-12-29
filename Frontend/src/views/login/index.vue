<template>
  <div class="h-screen w-screen flex justify-center items-center bg-background-1 relative">
    <div class="absolute top-4 bg-error text-white z-50 flex rounded-xl px-3 py-1" v-if="showErrorMessage">
      <i class="pi pi-times-circle text-xl"/>
      {{ errorMessage }}
    </div>
    <div class="bg-background-3 px-6 py-6 rounded-xl flex justify-center items-center flex-col drop-shadow-xl">
      <div class="text-primary-1 text-3xl mb-10">
        <img src="/src/assets/logo-white.webp" class="h-16 w-16" v-if="useDataStore().getDarkStatus">
        <img src="/src/assets/logo-black.webp" class="h-16 w-16" v-else>
      </div>
      <label class="text-info-3 z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
             :class="{'-mt-3 opacity-100' : username ,'ml-auto' : isRtl , 'mr-auto' : !isRtl}">{{
          local.username
        }}</label>
      <input type="text" :placeholder="local.username" v-model="username"
             class="z-20 w-72 md:w-96  shadow-lg mb-4 rounded-xl px-4 py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"/>
      <label class="text-info-3 z-0 px-2 pb-3 -mt-[35px] opacity-0 transition-all duration-200"
             :class="{'mt-0 opacity-100' : password ,'ml-auto' : isRtl , 'mr-auto' : !isRtl}">{{
          local.password
        }}</label>
      <div class="flex relative">
        <input :type="showPass ? 'text' : 'password'" :placeholder="local.password" v-model="password"
               class="z-20 w-72 md:w-96 shadow-lg mb-4 rounded-xl py-2 bg-background-2 text-info-3 placeholder:text-info-2 outline-none border-background-2 border-2 focus:border-primary-1 transition-all duration-150"
               :class="{'pr-4 pl-8' : isRtl , 'pl-4 pr-8' : !isRtl}"
        />
        <i class="pi pi-eye text-base z-20 text-info-2 absolute top-3 cursor-pointer"
                  :class="{'left-2' : isRtl , 'right-2' : !isRtl}" v-if="!showPass"
                  @click="showPass = !showPass"/>
        <i class="pi pi-eye-slash text-base z-20 text-info-2 absolute top-3 cursor-pointer"
                        :class="{'left-2' : isRtl , 'right-2' : !isRtl}" v-else
                        @click="showPass = !showPass"/>
      </div>
      <button class="rounded-xl bg-primary-3 w-full py-2 text-white hover:brightness-125 transition-all duration-150"
              @click="signIn">
        {{ local.signIn }}
      </button>
      <div class="relative mt-4">
        <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
             @click="showLangMenu = !showLangMenu">{{ useLocalization().getFlag }}
          {{ useLocalization().getLanguage.toUpperCase() }}
        </div>
        <div class="absolute left-0 rounded-xl flex flex-col w-max bg-primary-1 bg-opacity-20" v-if="showLangMenu">
          <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
               @click="changeLanguage(['🇮🇷','fa' , 'rtl'])">🇮🇷 FA
          </div>
          <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
               @click="changeLanguage(['🇺🇸','en' , 'ltr'])">🇺🇸 EN
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import {computed, onMounted, ref} from "vue";
import {useDataStore} from "../../store/dataStore.js";
import {useLocalization} from "../../store/localizationStore.js";
import axios from "axios";
import router from "../../router/index.js";

let showPass = ref(false)

let showLangMenu = ref(false)
let changeLanguage = (payload) => {
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
  document.cookie = `flag=${payload[0]};path=/`
  document.cookie = `language=${payload[1]};path=/`
  document.cookie = `direction=${payload[2]};path=/`
}

let local = computed(() => {
  return useLocalization().getLocal
})

let isRtl = computed(() => {
  return useLocalization().getDirection == 'rtl'
})

let showErrorMessage = ref(false)
let errorMessage = ref('')

let username = ref(undefined)
let password = ref(undefined)

const signIn = () => {
  if (username.value && password.value) {
    axios.post(`${useDataStore().getServerAddress}/authentication/login`,
        {
          username: username.value,
          password: password.value
        }
    ).then((response) => {
      useDataStore().setToken(response.data.token)
      document.cookie = `token=${response.data.token};path=/`
      document.cookie = `isDark=${useDataStore().getDarkStatus};path=/`
      document.cookie = `flag=${useLocalization().getFlag};path=/`
      document.cookie = `language=${useLocalization().getLanguage};path=/`
      document.cookie = `direction=${useLocalization().getDirection};path=/`
      if (response.data.role === 'Admin')
        router.push('/admin')
      else if (response.data.role === 'Customer' || response.data.role === 'SuperCustomer')
        router.push('/customer')
    }).catch((error) => {
      errorMessage.value = local.value.errorLoggingIn
      showErrorMessage.value = true;
      setTimeout(() => {
        showErrorMessage.value = false
      }, 2000)
    })
  } else {
    errorMessage.value = local.value.errorFieldsOfLogin
    showErrorMessage.value = true
    setTimeout(() => {
      showErrorMessage.value = false
    }, 2000)
  }
}

onMounted(() => {
  if (document.cookie) {
    let cookie = document.cookie.split('; ');
    let lang = new Array(3)
    cookie.forEach((data) => {
      let value = data.split('=')
      if (value[0] === 'flag') {
        lang[0] = value[1]
      } else if (value[0] === 'language') {
        if(value[1].indexOf('[') !== -1){
          lang = ['🇮🇷','fa' , 'rtl']
          document.cookie = `flag=${lang[0]};path=/`
          document.cookie = `language=${lang[1]};path=/`
          document.cookie = `direction=${lang[2]};path=/`
        }
        else {
          lang[1] = value[1]
        }
      } else if (value[0] === 'direction') {
        lang[2] = value[1]
      } else if (value[0] === 'isDark') {
        useDataStore().setDarkStatus(value[1] === 'true')
      } else if (value[0] === 'token') {
        useDataStore().setToken(value[1])
      }
    })
    useLocalization().changeLanguage(lang)
    axios.get(`${useDataStore().getServerAddress}/authentication/get-role`,
        {
          headers: {
            Authorization: useDataStore().getToken
          }
        }
    ).then((response) => {
      if (response.data === 'Admin')
        router.push('/admin')
      else if (response.data === 'Customer' || response.data === 'SuperCustomer')
        router.push('/customer')
    })
  }
})

</script>