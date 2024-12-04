package com.infinityinsurance.user.entity;

/**
 * Enum representing the KYC (Know Your Customer) status.
 * This is used to track the status of the user's verification process.
 */
public enum KycStatus {
    PENDING,   // The user's KYC process is pending
    VERIFIED,  // The user has been successfully verified
    FAILED     // The user verification has failed
}
