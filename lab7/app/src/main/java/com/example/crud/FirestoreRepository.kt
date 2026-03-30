package com.example.crud

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects


class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val coursesCollection = db.collection("Courses")


    fun insertCourse(
        course: Course,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        coursesCollection.add(course)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun getCourses(
        onSuccess: (List<Course>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        coursesCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val list = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Course::class.java)?.apply {
                        courseID = doc.id // Important: map the Firestore document ID back to our object
                    }
                }
                onSuccess(list)
            }
            .addOnFailureListener { onFailure(it) }
    }


    fun updateCourse(
        course: Course,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val courseID = course.courseID ?: return
        coursesCollection.document(courseID).set(course)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }


    fun deleteCourse(
        courseID: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        coursesCollection.document(courseID).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
