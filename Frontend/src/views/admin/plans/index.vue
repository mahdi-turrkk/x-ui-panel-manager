<template>
  <admin-layout>
    <plan-dialog :show-dialog="showPlanDialog" @close-dialog="showPlanDialog = false"
                 @plan-added="getPlans" :type="type" :plan="editPlan"/>
    <delete-confirmation-dialog :show-dialog="showDeleteConfirmationDialog" title="plans" :data="deletePlan" @close-dialog="showDeleteConfirmationDialog = false" @delete-complete="getPlans"/>
    <div class=" rounded-xl w-full py-3 px-2 md:px-4 flex justify-between items-center">
      <div class="text-info-3 font-bold text-lg">{{ local.plans }}</div>
      <button
          @click="()=>{type = 'new';showPlanDialog = true}"
          class="text-xs md:text-sm mx-2 outline-none border-2 rounded-xl border-success bg-success bg-opacity-20 text-success px-2 md:px-6 py-2 flex space-x-1 items-center">
        <plus-icon class="w-4 h-4"/>
        {{ local.add }} {{ local.plan }}
      </button>
    </div>
    <div class="flex justify-center mt-6" v-if="loading">
      <loader/>
    </div>
    <div class="rounded-xl w-full py-3 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4" v-else>
      <plan-card v-for="plan in plans" :plan="plan" @edit-plan="openEditPlanDialog" @delete-plan="openDeleteConfirmationDialog"/>
    </div>
    <div class="flex" v-if="!loading">
      <div
          class="w-8 h-8 rounded-xl bg-primary-1 bg-opacity-20 flex justify-center items-center mx-1 text-info-3 cursor-pointer transition-all duration-300"
          v-for="i in pages" :class="{'bg-opacity-50' : onboarding === i}" @click="onboarding = i">{{ i }}
      </div>
    </div>
  </admin-layout>
</template>

<script setup>
import AdminLayout from "../../../layouts/adminLayout.vue";
import {PlusIcon} from "@heroicons/vue/24/solid/index.js";
import {computed, onMounted, reactive, ref} from "vue";
import {useLocalization} from "../../../store/localizationStore.js";
import PlanCard from "../../../components/planCard.vue";
import axios from "axios";
import {useDataStore} from "../../../store/dataStore.js";
import Loader from "../../../components/loader.vue";
import AdminAddDialog from "../../../components/adminAddDialog.vue";
import PlanDialog from "../../../components/planDialog.vue";
import DeleteConfirmationDialog from "../../../components/deleteConfirmationDialog.vue";

const local = computed(() => {
  return useLocalization().getLocal
})

let pages = ref(1)
let onboarding = ref(1)
let loading = ref(true)
let showPlanDialog = ref(false)
let type = ref('new')
let editPlan = reactive({})
let deletePlan = reactive({})
let showDeleteConfirmationDialog = ref(false)

let plans = ref([
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
  {totalFlow: 30, periodLength: 30, price: 50000},
])

const getPlans = () => {
  loading.value = false
  axios.get(`${useDataStore().getServerAddress}/plans/get-all?size=12&page=${onboarding.value - 1}`,
      {
        headers: {
          Authorization: useDataStore().getToken
        }
      })
      .then((response) => {
        pages.value = response.data.totalPages
        plans.value = response.data.content
        loading.value = false
      })
}

onMounted(() => {
  getPlans()
})

const openEditPlanDialog = (payload) => {
  type.value = 'edit'
  editPlan.value = payload
  showPlanDialog.value = true
}

const openDeleteConfirmationDialog = (payload) =>{
  deletePlan.value = payload
  showDeleteConfirmationDialog.value = true
}
</script>