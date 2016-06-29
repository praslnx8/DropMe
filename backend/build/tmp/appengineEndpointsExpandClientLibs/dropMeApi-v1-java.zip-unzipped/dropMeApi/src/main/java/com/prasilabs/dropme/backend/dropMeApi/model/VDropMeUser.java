/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-05-27 16:00:31 UTC)
 * on 2016-06-29 at 06:59:47 UTC 
 * Modify at your own risk.
 */

package com.prasilabs.dropme.backend.dropMeApi.model;

/**
 * Model definition for VDropMeUser.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the dropMeApi. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class VDropMeUser extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer age;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime created;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String gender;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String hash;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String location;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String loginType;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String mobile;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean mobileVerified;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String picture;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<java.lang.String> roles;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Boolean verified;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getAge() {
    return age;
  }

  /**
   * @param age age or {@code null} for none
   */
  public VDropMeUser setAge(java.lang.Integer age) {
    this.age = age;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getCreated() {
    return created;
  }

  /**
   * @param created created or {@code null} for none
   */
  public VDropMeUser setCreated(com.google.api.client.util.DateTime created) {
    this.created = created;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public VDropMeUser setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getGender() {
    return gender;
  }

  /**
   * @param gender gender or {@code null} for none
   */
  public VDropMeUser setGender(java.lang.String gender) {
    this.gender = gender;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getHash() {
    return hash;
  }

  /**
   * @param hash hash or {@code null} for none
   */
  public VDropMeUser setHash(java.lang.String hash) {
    this.hash = hash;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public VDropMeUser setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLocation() {
    return location;
  }

  /**
   * @param location location or {@code null} for none
   */
  public VDropMeUser setLocation(java.lang.String location) {
    this.location = location;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLoginType() {
    return loginType;
  }

  /**
   * @param loginType loginType or {@code null} for none
   */
  public VDropMeUser setLoginType(java.lang.String loginType) {
    this.loginType = loginType;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMobile() {
    return mobile;
  }

  /**
   * @param mobile mobile or {@code null} for none
   */
  public VDropMeUser setMobile(java.lang.String mobile) {
    this.mobile = mobile;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getMobileVerified() {
    return mobileVerified;
  }

  /**
   * @param mobileVerified mobileVerified or {@code null} for none
   */
  public VDropMeUser setMobileVerified(java.lang.Boolean mobileVerified) {
    this.mobileVerified = mobileVerified;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * @param name name or {@code null} for none
   */
  public VDropMeUser setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPicture() {
    return picture;
  }

  /**
   * @param picture picture or {@code null} for none
   */
  public VDropMeUser setPicture(java.lang.String picture) {
    this.picture = picture;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<java.lang.String> getRoles() {
    return roles;
  }

  /**
   * @param roles roles or {@code null} for none
   */
  public VDropMeUser setRoles(java.util.List<java.lang.String> roles) {
    this.roles = roles;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Boolean getVerified() {
    return verified;
  }

  /**
   * @param verified verified or {@code null} for none
   */
  public VDropMeUser setVerified(java.lang.Boolean verified) {
    this.verified = verified;
    return this;
  }

  @Override
  public VDropMeUser set(String fieldName, Object value) {
    return (VDropMeUser) super.set(fieldName, value);
  }

  @Override
  public VDropMeUser clone() {
    return (VDropMeUser) super.clone();
  }

}
