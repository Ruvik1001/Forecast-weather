package com.example.studentweatherapp.data

//suspend fun getCurrentLocation(): Location? {
//    val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//    val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
//        context,
//        Manifest.permission.ACCESS_COARSE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//
//    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//    val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
//            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//    if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
//        return null
//    }
//
//    val locationClient = LocationServices.getFusedLocationProviderClient(context)
//
//    return suspendCancellableCoroutine { cont ->
//        locationClient.lastLocation.apply {
//            if(isComplete) {
//                if(isSuccessful) {
//                    cont.resume(result)
//                } else {
//                    cont.resume(null)
//                }
//                return@suspendCancellableCoroutine
//            }
//            addOnSuccessListener {
//                cont.resume(it)
//            }
//            addOnFailureListener {
//                cont.resume(null)
//            }
//            addOnCanceledListener {
//                cont.cancel()
//            }
//        }
//    }
//}

//@OptIn(ExperimentalPermissionsApi::class)
//private fun requestLocationPermissions() {
//    val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//    ActivityCompat.requestPermissions(App.context, permissions)
//}
