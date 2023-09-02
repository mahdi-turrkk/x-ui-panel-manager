<template>
  <div class="grid grid-cols-12">
    <div class="col-span-12 relative z-20 md:hidden">
      <div class="px-4 py-4">
        <div class="md:hidden flex items-center justify-between">
          <div class="flex">
            <button @click="isHamburgerOpen = !isHamburgerOpen" class="px-2">
              <div class="border-t-2 border-t-info-3 w-8 my-2 duration-300 transition-all"
                   :class="{'rotate-45 translate-y-3' : isHamburgerOpen}"/>
              <div class="border-t-2 w-8 my-2 duration-300 transition-all"
                   :class="{'border-t-transparent' : isHamburgerOpen , 'border-t-info-3' : !isHamburgerOpen}"/>
              <div class="border-t-2 border-t-info-3 w-8 my-2 duration-300 transition-all"
                   :class="{'-rotate-45 -translate-y-2' : isHamburgerOpen}"/>
            </button>
            <div class="relative px-2">
              <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
                   @click="showLangMenu = !showLangMenu">{{ useLocalization().getFlag }}
                {{ useLocalization().getLanguage.toUpperCase() }}
              </div>
              <div class="absolute left-0 rounded-xl w-max bg-background-3"
                   v-if="showLangMenu">
                <div class="flex flex-col rounded-xl bg-primary-1 bg-opacity-20">
                  <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                       @click="changeLanguage(['🇮🇷','fa' , 'rtl'])">🇮🇷 FA
                  </div>
                  <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                       @click="changeLanguage(['🇺🇸','en' , 'ltr'])">🇺🇸 EN
                  </div>
                </div>
              </div>
            </div>
            <button @click="changeThemeStatus"
                    class="p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200">
              <sun-icon class="w-6 h-6 text-info-3" v-if="isDark"/>
              <moon-icon class="w-6 h-6 text-info-3" v-if="!isDark"/>
            </button>
          </div>
          <img :src="logoSrc" class="mx-2 w-10 h-10 cursor-pointer" @click="router.push('/')">
        </div>
      </div>
    </div>
    <div
        class="pt-14 md:pt-0 px-4 col-span-12 md:col-span-3 lg:col-span-2 bg-background-3 h-screen absolute md:sticky top-0 right-0 left-0 bottom-0 z-10 flex flex-col space-y-4"
        v-if="isHamburgerOpen || isBigScreen">
      <div class="hidden md:flex items-center justify-between mt-4">
        <img :src="logoSrc" class="w-10 h-10 cursor-pointer" @click="router.push('/')">
        <div class="flex space-x-2 items-center">
          <div class="relative">
            <div class="text-info-3 bg-primary-1 bg-opacity-0 hover:bg-opacity-20 p-2 rounded-xl cursor-pointer"
                 @click="showLangMenu = !showLangMenu">{{ useLocalization().getFlag }}
              {{ useLocalization().getLanguage.toUpperCase() }}
            </div>
            <div class="absolute left-0 rounded-xl bg-background-3" v-if="showLangMenu">
              <div class="flex flex-col w-max rounded-xl bg-primary-1 bg-opacity-20">
                <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                     @click="changeLanguage(['🇮🇷','fa' , 'rtl'])">🇮🇷 FA
                </div>
                <div class="text-info-3 px-3 py-2 hover:bg-primary-1 hover:bg-opacity-60 cursor-pointer rounded-xl"
                     @click="changeLanguage(['🇺🇸','en' , 'ltr'])">🇺🇸 EN
                </div>
              </div>
            </div>
          </div>
          <button @click="changeThemeStatus"
                  class="p-2 rounded-xl bg-primary-1 bg-opacity-0 hover:bg-opacity-20 transition-all duration-200">
            <sun-icon class="w-6 h-6 text-info-3" v-if="isDark"/>
            <moon-icon class="w-6 h-6 text-info-3" v-if="!isDark"/>
          </button>
        </div>
      </div>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': ($route.fullPath == '/admin' && isRtl) , 'bg-opacity-20 px-2 ml-2': ($route.fullPath == '/admin' && !isRtl) , 'hover:mr-2' : isRtl , 'hover:ml-2' : !isRtl}"
          @click="router.push('/admin')">
        <squares2-x2-icon class="w-6 h-6"/>
        <div class="px-3">{{ local.dashboard }}</div>
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': ($route.fullPath == '/admin/servers' && isRtl) , 'bg-opacity-20 px-2 ml-2': ($route.fullPath == '/admin/servers' && !isRtl) , 'hover:mr-2' : isRtl , 'hover:ml-2' : !isRtl}"
          @click="router.push('/admin/servers')">
        <server-stack-icon class="w-6 h-6"/>
        <div class="px-3">{{ local.servers }}</div>
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': ($route.fullPath == '/admin/customers' && isRtl) , 'bg-opacity-20 px-2 ml-2': ($route.fullPath == '/admin/customers' && !isRtl) , 'hover:mr-2' : isRtl , 'hover:ml-2' : !isRtl}"
          @click="router.push('/admin/customers')">
        <UserGroupIcon class="w-6 h-6"/>
        <div class="px-3">{{ local.customers }}</div>
      </button>
      <button
          class="text-info-3 text-lg rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 transition-all duration-200"
          :class="{'bg-opacity-20 px-2 mr-2': ($route.fullPath == '/admin/subscription' && isRtl) , 'bg-opacity-20 px-2 ml-2': ($route.fullPath == '/admin/subscription' && !isRtl) , 'hover:mr-2' : isRtl , 'hover:ml-2' : !isRtl}"
          @click="router.push('/admin/subscriptions')">
        <clipboard-document-list-icon class="w-6 h-6"/>
        <div class="px-3">{{ local.subscriptions }}</div>
      </button>
      <button
          class="text-info-3 text-xl rounded-xl py-3 flex items-center bg-primary-1 bg-opacity-0 hover:bg-opacity-20 hover:px-2 transition-all duration-200"
          :class="{'hover:mr-2' : isRtl , 'hover:ml-2' : !isRtl}"
          @click="router.push('/')">
        <arrow-left-on-rectangle-icon class="w-6 h-6"/>
        <div class="px-3">{{ local.signOut }}</div>
      </button>
    </div>
    <div class="col-span-12 md:col-span-9 lg:col-span-10 relative z-0 pt-4 px-4">
      <slot/>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from "vue";
import {displayHelper} from "../helpers/displayHelper.js";
import {
  SunIcon,
  MoonIcon,
  ServerStackIcon,
  Squares2X2Icon,
  UserGroupIcon,
  ClipboardDocumentListIcon,
  ArrowLeftOnRectangleIcon
} from "@heroicons/vue/24/outline/index.js";
import {useDataStore} from "../store/dataStore.js";
import router from "../router/index.js";
import {useLocalization} from "../store/localizationStore.js";

let windowWidth = ref(0)

onMounted(() => {
  windowWidth.value = window.innerWidth
  window.addEventListener('resize', () => {
    windowWidth.value = window.innerWidth
  })
})

const isBigScreen = computed(() => {
  return displayHelper(windowWidth.value).mdAndUp
})
let isHamburgerOpen = ref(false)

let isDark = computed(() => {
  return useDataStore().getDarkStatus
})

const changeThemeStatus = () => {
  useDataStore().changeDarkStatus()
}

let logoSrc = computed(() => {
  return useDataStore().getDarkStatus ? '../src/assets/logo-white.png' : '../src/assets/logo-black.png'
})

let showLangMenu = ref(false)
let changeLanguage = (payload) => {
  showLangMenu.value = false
  useLocalization().changeLanguage(payload)
}

let local = computed(() => {
  return useLocalization().getLocal
})

let isRtl = computed(() => {
  return useLocalization().getDirection == 'rtl'
})
</script>