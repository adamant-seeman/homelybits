package com.example.homelybites.utils;

import com.example.homelybites.models.Dish;
import com.example.homelybites.models.Kitchen;
import com.example.homelybites.models.Order;
import com.example.homelybites.models.Review;
import com.example.homelybites.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class FirebaseHelper {

    private static FirebaseHelper instance;
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;

    private FirebaseHelper() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    // ===================== AUTH =====================

    public FirebaseAuth getAuth() { return auth; }

    public FirebaseUser getCurrentUser() { return auth.getCurrentUser(); }

    public boolean isLoggedIn() { return auth.getCurrentUser() != null; }

    public void signUp(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signIn(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void sendPasswordReset(String email, OnCompleteListener<Void> listener) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public void signOut() { auth.signOut(); }

    // ===================== USERS =====================

    public void saveUser(User user, OnCompleteListener<Void> listener) {
        db.collection("users").document(user.getUid())
                .set(user).addOnCompleteListener(listener);
    }

    public void getUser(String uid, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("users").document(uid).get().addOnCompleteListener(listener);
    }

    public void updateUserField(String uid, String field, Object value, OnCompleteListener<Void> listener) {
        db.collection("users").document(uid).update(field, value).addOnCompleteListener(listener);
    }

    // ===================== KITCHENS =====================

    public void saveKitchen(Kitchen kitchen, OnCompleteListener<Void> listener) {
        db.collection("kitchens").document(kitchen.getKitchenId())
                .set(kitchen).addOnCompleteListener(listener);
    }

    public void getAllKitchens(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("kitchens").get().addOnCompleteListener(listener);
    }

    public void getKitchen(String kitchenId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("kitchens").document(kitchenId).get().addOnCompleteListener(listener);
    }

    public void getKitchenByCook(String cookUid, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("kitchens").whereEqualTo("cookUid", cookUid).get().addOnCompleteListener(listener);
    }

    public void searchKitchens(String query, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("kitchens")
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .get().addOnCompleteListener(listener);
    }

    // ===================== DISHES =====================

    public void saveDish(Dish dish, OnCompleteListener<Void> listener) {
        db.collection("dishes").document(dish.getDishId())
                .set(dish).addOnCompleteListener(listener);
    }

    public void getDishesByKitchen(String kitchenId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("dishes")
                .whereEqualTo("kitchenId", kitchenId)
                .get().addOnCompleteListener(listener);
    }

    public void getDishesByDay(String kitchenId, String day, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("dishes")
                .whereEqualTo("kitchenId", kitchenId)
                .whereEqualTo("day", day)
                .get().addOnCompleteListener(listener);
    }

    public void deleteDish(String dishId, OnCompleteListener<Void> listener) {
        db.collection("dishes").document(dishId).delete().addOnCompleteListener(listener);
    }

    // ===================== ORDERS =====================

    public String generateOrderId() {
        return db.collection("orders").document().getId();
    }

    public void saveOrder(Order order, OnCompleteListener<Void> listener) {
        db.collection("orders").document(order.getOrderId())
                .set(order).addOnCompleteListener(listener);
    }

    public void getOrdersByCustomer(String customerId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("orders")
                .whereEqualTo("customerId", customerId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(listener);
    }

    public void getOrdersByKitchen(String kitchenId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("orders")
                .whereEqualTo("kitchenId", kitchenId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(listener);
    }

    public void getPendingOrdersByKitchen(String kitchenId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("orders")
                .whereEqualTo("kitchenId", kitchenId)
                .whereEqualTo("status", "waiting")
                .get().addOnCompleteListener(listener);
    }

    public void updateOrderStatus(String orderId, String status, OnCompleteListener<Void> listener) {
        db.collection("orders").document(orderId)
                .update("status", status).addOnCompleteListener(listener);
    }

    // ===================== REVIEWS =====================

    public void saveReview(Review review, OnCompleteListener<Void> listener) {
        db.collection("reviews").document(review.getReviewId())
                .set(review).addOnCompleteListener(listener);
    }

    public void getReviewsByKitchen(String kitchenId, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("reviews")
                .whereEqualTo("kitchenId", kitchenId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(listener);
    }

    // ===================== STORAGE =====================

    public void uploadImage(String path, Uri imageUri, OnCompleteListener<Uri> listener) {
        StorageReference ref = storage.getReference().child(path);
        ref.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                ref.getDownloadUrl().addOnCompleteListener(listener)
        );
    }

    // ===================== HELPERS =====================

    public String generateId(String collection) {
        return db.collection(collection).document().getId();
    }

    public FirebaseFirestore getDb() { return db; }
}
