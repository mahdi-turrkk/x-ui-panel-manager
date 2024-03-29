import {defineStore} from 'pinia'

export const useLocalization = defineStore('localize', {
    state: () => {
        return {
            language: 'fa',
            flag: '🇮🇷',
            direction: 'rtl',
            locals: {
                fa: {
                    username: 'نام‌کاربری',
                    password: 'رمز‌عبور',
                    firstName: 'نام',
                    lastName: 'نام‌خانوادگی',
                    email: 'ایمیل',
                    phoneNumber: 'شماره تماس',
                    address: 'آدرس',
                    signIn: 'ورود',
                    signOut: 'خروج',
                    dashboard: 'داشبورد',
                    servers: 'سرورها',
                    customers: 'مشتری‌ها',
                    customer: 'مشتری‌',
                    subscriptions: 'اشتراک‌ها',
                    subscription: 'اشتراک‌',
                    new: 'جدید',
                    edit: 'ویرایش',
                    delete: 'حذف',
                    update: 'بروزرسانی',
                    add: 'افزودن',
                    id: 'شناسه',
                    serverUrl: 'آدرس سرور',
                    server: 'سرور',
                    information: 'اطلاعات',
                    generatable: 'قابلیت تولید',
                    actions: 'عملیات',
                    active: 'فعال',
                    activate: 'فعالسازی',
                    deactivate: 'غیرفعالسازی',
                    inactive: 'غیرفعال',
                    inbounds: 'دسته کانفیگ‌ها',
                    inbound: 'دسته کانفیگ',
                    title: 'عنوان',
                    port: 'پورت',
                    status: 'وضعیت',
                    save: 'ذخیره',
                    cancel: 'لغو',
                    show: 'نمایش',
                    url: 'لینک',
                    startEndDate: 'تاریخ شروع/پایان',
                    usage: 'مصرف',
                    renew: 'تمدید',
                    totalFlow: 'حجم کل',
                    ipLimit: 'تعداد کاربر',
                    periodLength: 'طول دوره (روز)',
                    clickToCopy: 'برای کپی روی بارکد یا لینک کلیک کنید.',
                    subscriptionUrl: 'لینک اشتراک',
                    copySuccessful: 'لینک کپی شد',
                    copyUnsuccessful: 'خطایی رخ داده',
                    subLookUp: 'استعلام وضعیت اشتراک',
                    startDate: 'تاریخ شروع',
                    expireDate: 'تاریخ انقضا',
                    date: 'تاریخ',
                    totalUsed: 'حجم مصرف‌شده',
                    enterSubToSearch: 'برای گزارش‌گیری لینک اشتراک را وارد کرده و روی ذره‌بین کلیک کنید.',
                    database: 'پایگاه داده',
                    upload: 'آپلود',
                    download: 'دانلود',
                    activitySummary: 'خلاصه عملکرد',
                    lastMonthSells: 'فروش ماه گذشته',
                    totalSells: 'فروش کلی',
                    totalUpload: 'کل حجم آپلودی',
                    totalDownload: 'کل حجم دانلودی',
                    errorLoggingIn: 'مشکلی در ورود به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfLogin: 'فیلد هارا به صورت درست پر کنید',
                    errorSavingCustomer: 'مشکلی در ثبت مشتری به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfCustomer: 'فیلد هارا به صورت درست پر کنید',
                    customerSavedSuccessfully: 'مشتری با موفقیت ثبت شد',
                    errorSavingServer: 'مشکلی در ثبت سرور به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfServer: 'فیلد هارا به صورت درست پر کنید',
                    serverSavedSuccessfully: 'سرور با موفقیت ثبت شد',
                    inboundsLoadFailed: 'عملیات با خطا مواجه شد.دوباره امتحان کنید.',
                    inboundsLoadSuccessfully: 'دسته کانفیگ ها با موفقیت بروزرسانی شدند.',
                    subscriptionSavedSuccessfully: 'اشتراک /ها با موفقیت ثبت شد.',
                    errorSavingSubscription: 'مشکلی در ثبت اشتراک /ها به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfSubscription: 'فیلد هارا به صورت درست پر کنید.',
                    number: 'تعداد',
                    databaseUploadSuccessful: 'پایگاه داده با موفقیت آپلود شد.',
                    databaseUploadFailed: 'آپلود پایگاه داده با خطا مواجه شد دوباره امتحان کنید.',
                    syncWithPanel: 'همگام سازی اشتراک ها',
                    syncWithPanelSuccessful: 'اشتراک ها با پنل همگام شدند.',
                    syncWithPanelFailed: 'همگام‌سازی با پنل با خطا مواجه شد.دوباره سعی کنید.',
                    noRecords: 'رکوردی برای نمایش وجود ندارد',
                    syncSubs: 'همگام‌سازی اشتراک ها',
                    subsSyncedSuccessfully: 'اشتراک‌ها با موفقیت همگام شدند.',
                    syncSubsFailed: 'همگام‌سازی اشتراک‌ها با خطا مواجه شد.دوباره سعی کنید.',
                    days: 'روز',
                    admins: 'ادمین ها',
                    admin: 'ادمین',
                    errorSavingAdmin: 'مشکلی در ثبت ادمین به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfAdmin: 'فیلد هارا به صورت درست پر کنید',
                    adminSavedSuccessfully: 'ادمین با موفقیت ثبت شد',
                    changePassword: 'تغییر رمزعبور',
                    changeTheme: 'تغییر پوسته',
                    oldPassword: 'رمز قبلی',
                    newPassword: 'رمز جدید',
                    passwordChangedSuccessfully: 'رمزعبور با موفقیت تغییر یافت.',
                    errorChangingPassword: 'مشکلی در تغییر رمزعبور به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfPassword: 'فیلد هارا به صورت درست پر کنید.',
                    remaining: 'باقی‌مانده',
                    plans: 'طرح‌ها',
                    plan: 'طرح',
                    price: 'قیمت',
                    errorSavingPlan: 'مشکلی در ثبت طرح به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfPlan: 'فیلد هارا به صورت درست پر کنید',
                    planSavedSuccessfully: 'طرح با موفقیت ثبت شد',
                    deletedSuccessfully: 'حذف با موفقیت انجام شد.',
                    errorOccurredWhileDeleting: 'مشکلی پیش آمده.دوباره تلاش کنید.',
                    deleteConfirmation: "حذف",
                    deleteConfirmationMessage: "آیا از حذف مطمئن هستید؟",
                    markAsPaid: 'علامت گذاری به عنوان پرداخت شده',
                    markAsNotPaid: 'علامت گذاری به عنوان پرداخت نشده',
                    payStatus: 'وضعیت پرداخت',
                    paid: 'پرداخت شده',
                    notPaid: 'پرداخت نشده',
                    history: 'تاریخچه',
                    errorGettingHistory: 'مشکلی در دریافت تاریخچه تمدید پیش آمده،دوباره تلاش کنید.',
                    renewHistory: 'تاریخچه تمدید ها',
                    errorOccurredWhileChangingStatus: 'مشکلی پیش آمده.دوباره تلاش کنید.',
                    downloadReport: 'دانلود گزارش',
                    debt: "میزان بدهی",
                    soldFlow: "میزان فروش",
                    superCustomer: 'فروشنده',
                    superCustomers: 'فروشنده‌ها',
                    pricePerUse: 'قیمت به ازای هر گیگ',
                    userDetail: 'اطلاعات کاربر',
                    payAmount: 'مقدار پرداختی (تومان)',
                    errorSavingPayment: 'مشکلی در ثبت پرداخت به وجود آمده.دوباره تلاش کنید.',
                    errorFieldsOfPayment: 'فیلد هارا به صورت درست پر کنید.',
                    addPayment: 'افزودن پرداختی',
                    invalidFlowOrPeriod: 'مقدار فیلد حجم کل یا مدت را به صورت صحیح (اعداد مثبت) وارد کنید.',
                    requestInProgress: 'درخواست در حال پردازش میباشد لطفا منتظر بمانید.',
                    clientsSetting: 'تنظیمات مخاطبین',
                    clients: 'مخاطبین',
                    fragment: 'فرگمنت',
                    fragmentLength: "طول فرگمنت",
                    fragmentInterval: 'بازه فرگمنت',
                    sendToClient: 'ارسال به مخاطب',
                    errorOccurredWhileSavingOsSetting: 'مشکلی پیش آمده.دوباره تلاش کنید.'
                },
                en: {
                    username: 'Username',
                    password: 'Password',
                    firstName: 'First name',
                    lastName: 'Last name',
                    email: 'Email',
                    phoneNumber: 'Phone number',
                    address: 'Address',
                    signIn: 'Sign in',
                    signOut: 'Sign out',
                    dashboard: 'Dashboard',
                    servers: 'Servers',
                    customers: 'Customers',
                    customer: 'Customer',
                    subscriptions: 'Subscriptions',
                    subscription: 'Subscription',
                    new: 'new',
                    edit: 'Edit',
                    delete: 'Delete',
                    update: 'Update',
                    add: 'Add',
                    id: 'ID',
                    serverUrl: 'Server url',
                    server: 'Server',
                    information: 'Information',
                    generatable: 'Generatable',
                    actions: 'Actions',
                    active: 'Active',
                    activate: 'Activate',
                    deactivate: 'Deactivate',
                    inactive: 'Inactive',
                    inbounds: 'Inbounds',
                    inbound: 'Inbound',
                    title: 'title',
                    port: 'port',
                    status: 'status',
                    save: 'Save',
                    cancel: 'Cancel',
                    show: 'Show',
                    url: 'Url',
                    startEndDate: 'Start/End Date',
                    usage: 'Usage',
                    renew: 'renew',
                    totalFlow: 'Total flow',
                    ipLimit: 'Ip limit',
                    periodLength: 'Period length (days)',
                    clickToCopy: 'Click on QR code or link to copy link.',
                    subscriptionUrl: 'Subscription link',
                    copySuccessful: 'Link copied successfully',
                    copyUnsuccessful: 'Error occurred',
                    subLookUp: 'Subscription status lookup',
                    startDate: 'Start date',
                    expireDate: 'Expire date',
                    date: 'Date',
                    totalUsed: 'Total used',
                    enterSubToSearch: 'Enter subscription link and press magnifier to lookup subscription status.',
                    database: 'Database',
                    upload: 'Upload',
                    download: 'Download',
                    activitySummary: 'Activity summary',
                    lastMonthSells: 'Last month sells',
                    totalSells: 'Total sells',
                    totalUpload: 'Total upload',
                    totalDownload: 'Total download',
                    errorLoggingIn: 'Error occurred while logging in.Try again.',
                    errorFieldsOfLogin: 'Fill the field correctly.',
                    errorSavingCustomer: 'Error occurred while saving customer.Try again.',
                    errorFieldsOfCustomer: 'Fill the fields correctly.',
                    customerSavedSuccessfully: 'Customer saved successfully',
                    errorSavingServer: 'Error occurred while saving server.Try again.',
                    errorFieldsOfServer: 'Fill the fields correctly.',
                    serverSavedSuccessfully: 'Server saved successfully',
                    inboundsLoadFailed: 'Loading inbounds failed.Try again.',
                    inboundsLoadSuccessfully: 'Inbounds loaded successfully.',
                    subscriptionSavedSuccessfully: 'Subscription /s Saved Successfully.',
                    errorSavingSubscription: 'Error occurred while saving Subscription /s.Try again.',
                    errorFieldsOfSubscription: 'Fill the fields correctly.',
                    number: 'Number',
                    databaseUploadSuccessful: 'Database uploaded successfully',
                    databaseUploadFailed: 'Database upload failed.Try again.',
                    syncWithPanel: 'Sync subscriptions',
                    syncWithPanelSuccessful: 'Subscriptions synced with panel successfully.',
                    syncWithPanelFailed: 'Error occurred while syncing.Try again.',
                    noRecords: 'No records to show',
                    syncSubs: 'Sync subscriptions',
                    subsSyncedSuccessfully: 'Subscriptions synced successfully.',
                    syncSubsFailed: 'Error occurred while syncing.Try again.',
                    days: 'Days',
                    admins: 'Admins',
                    admin: 'Admin',
                    errorSavingAdmin: 'Error occurred while saving admin.Try again.',
                    errorFieldsOfAdmin: 'Fill the fields correctly.',
                    adminSavedSuccessfully: 'Admin saved successfully',
                    changePassword: 'Change Password',
                    changeTheme: 'Change Theme',
                    newPassword: 'New password',
                    oldPassword: 'Old password',
                    passwordChangedSuccessfully: 'Password Changed Successfully.',
                    errorChangingPassword: 'Error occurred while changing password.Try again.',
                    errorFieldsOfPassword: 'Fill the fields correctly.',
                    remaining: 'Remaining',
                    plans: 'Plans',
                    plan: 'Plan',
                    price: 'Price',
                    errorSavingPlan: 'Error occurred while saving plan.Try again.',
                    errorFieldsOfPlan: 'Fill the fields correctly.',
                    planSavedSuccessfully: 'Plan saved successfully.',
                    deletedSuccessfully: 'Deleted successfully.',
                    errorOccurredWhileDeleting: 'Error occurred while deletion.Try again.',
                    deleteConfirmation: "Delete confirmation",
                    deleteConfirmationMessage: "Are you sure to delete?",
                    markAsPaid: 'Mark as paid',
                    markAsNotPaid: 'Mark as not paid',
                    payStatus: 'Pay status',
                    paid: 'Paid',
                    notPaid: 'Not Paid',
                    history: 'History',
                    errorGettingHistory: 'Error occurred getting renew history,Try again.',
                    renewHistory: 'Renew history',
                    errorOccurredWhileChangingStatus: 'Error occurred while changing status.Try again.',
                    downloadReport: 'Download report',
                    debt: "Debt",
                    soldFlow: "Sold flow",
                    superCustomer: 'Vendor',
                    superCustomers: 'Vendors',
                    pricePerUse: 'Price per GB',
                    userDetail: 'User detail',
                    payAmount: 'Pay Amount (Tomans)',
                    errorSavingPayment: 'Error occurred while saving payment.Try again.',
                    errorFieldsOfPayment: 'Fill the fields correctly.',
                    addPayment: 'Add Payment',
                    invalidFlowOrPeriod: 'Fill the fields correctly. Flow and period must be positive numbers.',
                    requestInProgress: 'Request in progress. Please wait.',
                    clientsSetting: 'Clients Setting',
                    clients: 'Clients',
                    fragment: 'Fragment',
                    fragmentLength: "Fragment Length",
                    fragmentInterval: 'Fragment Interval',
                    sendToClient: 'Send to Client',
                    errorOccurredWhileSavingOsSetting: 'Error occurred while saving os setting.Try again.',
                },
            }
        }
    },
    getters: {
        getLanguage() {
            return this.language
        },
        getFlag() {
            return this.flag
        },
        getLocal() {
            return this.locals[this.language]
        },
        getDirection() {
            return this.direction
        },
    },
    actions: {
        changeLanguage(payload) {
            this.flag = payload[0]
            this.language = payload[1]
            this.direction = payload[2]
        },
    }
})