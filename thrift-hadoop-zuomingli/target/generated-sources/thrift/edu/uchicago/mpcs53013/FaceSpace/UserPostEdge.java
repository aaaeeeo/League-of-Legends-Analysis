/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package edu.uchicago.mpcs53013.FaceSpace;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2016-10-19")
public class UserPostEdge implements org.apache.thrift.TBase<UserPostEdge, UserPostEdge._Fields>, java.io.Serializable, Cloneable, Comparable<UserPostEdge> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserPostEdge");

  private static final org.apache.thrift.protocol.TField USER_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("user_id", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField POST_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("post_id", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField VISABLE_FIELD_DESC = new org.apache.thrift.protocol.TField("visable", org.apache.thrift.protocol.TType.BOOL, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new UserPostEdgeStandardSchemeFactory());
    schemes.put(TupleScheme.class, new UserPostEdgeTupleSchemeFactory());
  }

  public UserID user_id; // required
  public PostID post_id; // required
  public boolean visable; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    USER_ID((short)1, "user_id"),
    POST_ID((short)2, "post_id"),
    VISABLE((short)3, "visable");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // USER_ID
          return USER_ID;
        case 2: // POST_ID
          return POST_ID;
        case 3: // VISABLE
          return VISABLE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __VISABLE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.USER_ID, new org.apache.thrift.meta_data.FieldMetaData("user_id", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, UserID.class)));
    tmpMap.put(_Fields.POST_ID, new org.apache.thrift.meta_data.FieldMetaData("post_id", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, PostID.class)));
    tmpMap.put(_Fields.VISABLE, new org.apache.thrift.meta_data.FieldMetaData("visable", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserPostEdge.class, metaDataMap);
  }

  public UserPostEdge() {
  }

  public UserPostEdge(
    UserID user_id,
    PostID post_id,
    boolean visable)
  {
    this();
    this.user_id = user_id;
    this.post_id = post_id;
    this.visable = visable;
    setVisableIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserPostEdge(UserPostEdge other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetUser_id()) {
      this.user_id = new UserID(other.user_id);
    }
    if (other.isSetPost_id()) {
      this.post_id = new PostID(other.post_id);
    }
    this.visable = other.visable;
  }

  public UserPostEdge deepCopy() {
    return new UserPostEdge(this);
  }

  @Override
  public void clear() {
    this.user_id = null;
    this.post_id = null;
    setVisableIsSet(false);
    this.visable = false;
  }

  public UserID getUser_id() {
    return this.user_id;
  }

  public UserPostEdge setUser_id(UserID user_id) {
    this.user_id = user_id;
    return this;
  }

  public void unsetUser_id() {
    this.user_id = null;
  }

  /** Returns true if field user_id is set (has been assigned a value) and false otherwise */
  public boolean isSetUser_id() {
    return this.user_id != null;
  }

  public void setUser_idIsSet(boolean value) {
    if (!value) {
      this.user_id = null;
    }
  }

  public PostID getPost_id() {
    return this.post_id;
  }

  public UserPostEdge setPost_id(PostID post_id) {
    this.post_id = post_id;
    return this;
  }

  public void unsetPost_id() {
    this.post_id = null;
  }

  /** Returns true if field post_id is set (has been assigned a value) and false otherwise */
  public boolean isSetPost_id() {
    return this.post_id != null;
  }

  public void setPost_idIsSet(boolean value) {
    if (!value) {
      this.post_id = null;
    }
  }

  public boolean isVisable() {
    return this.visable;
  }

  public UserPostEdge setVisable(boolean visable) {
    this.visable = visable;
    setVisableIsSet(true);
    return this;
  }

  public void unsetVisable() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __VISABLE_ISSET_ID);
  }

  /** Returns true if field visable is set (has been assigned a value) and false otherwise */
  public boolean isSetVisable() {
    return EncodingUtils.testBit(__isset_bitfield, __VISABLE_ISSET_ID);
  }

  public void setVisableIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __VISABLE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case USER_ID:
      if (value == null) {
        unsetUser_id();
      } else {
        setUser_id((UserID)value);
      }
      break;

    case POST_ID:
      if (value == null) {
        unsetPost_id();
      } else {
        setPost_id((PostID)value);
      }
      break;

    case VISABLE:
      if (value == null) {
        unsetVisable();
      } else {
        setVisable((Boolean)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case USER_ID:
      return getUser_id();

    case POST_ID:
      return getPost_id();

    case VISABLE:
      return isVisable();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case USER_ID:
      return isSetUser_id();
    case POST_ID:
      return isSetPost_id();
    case VISABLE:
      return isSetVisable();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof UserPostEdge)
      return this.equals((UserPostEdge)that);
    return false;
  }

  public boolean equals(UserPostEdge that) {
    if (that == null)
      return false;

    boolean this_present_user_id = true && this.isSetUser_id();
    boolean that_present_user_id = true && that.isSetUser_id();
    if (this_present_user_id || that_present_user_id) {
      if (!(this_present_user_id && that_present_user_id))
        return false;
      if (!this.user_id.equals(that.user_id))
        return false;
    }

    boolean this_present_post_id = true && this.isSetPost_id();
    boolean that_present_post_id = true && that.isSetPost_id();
    if (this_present_post_id || that_present_post_id) {
      if (!(this_present_post_id && that_present_post_id))
        return false;
      if (!this.post_id.equals(that.post_id))
        return false;
    }

    boolean this_present_visable = true;
    boolean that_present_visable = true;
    if (this_present_visable || that_present_visable) {
      if (!(this_present_visable && that_present_visable))
        return false;
      if (this.visable != that.visable)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_user_id = true && (isSetUser_id());
    list.add(present_user_id);
    if (present_user_id)
      list.add(user_id);

    boolean present_post_id = true && (isSetPost_id());
    list.add(present_post_id);
    if (present_post_id)
      list.add(post_id);

    boolean present_visable = true;
    list.add(present_visable);
    if (present_visable)
      list.add(visable);

    return list.hashCode();
  }

  @Override
  public int compareTo(UserPostEdge other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetUser_id()).compareTo(other.isSetUser_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUser_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.user_id, other.user_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPost_id()).compareTo(other.isSetPost_id());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPost_id()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.post_id, other.post_id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetVisable()).compareTo(other.isSetVisable());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetVisable()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.visable, other.visable);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("UserPostEdge(");
    boolean first = true;

    sb.append("user_id:");
    if (this.user_id == null) {
      sb.append("null");
    } else {
      sb.append(this.user_id);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("post_id:");
    if (this.post_id == null) {
      sb.append("null");
    } else {
      sb.append(this.post_id);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("visable:");
    sb.append(this.visable);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (user_id == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'user_id' was not present! Struct: " + toString());
    }
    if (post_id == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'post_id' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'visable' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class UserPostEdgeStandardSchemeFactory implements SchemeFactory {
    public UserPostEdgeStandardScheme getScheme() {
      return new UserPostEdgeStandardScheme();
    }
  }

  private static class UserPostEdgeStandardScheme extends StandardScheme<UserPostEdge> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserPostEdge struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // USER_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.user_id = new UserID();
              struct.user_id.read(iprot);
              struct.setUser_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // POST_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.post_id = new PostID();
              struct.post_id.read(iprot);
              struct.setPost_idIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // VISABLE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.visable = iprot.readBool();
              struct.setVisableIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetVisable()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'visable' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserPostEdge struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.user_id != null) {
        oprot.writeFieldBegin(USER_ID_FIELD_DESC);
        struct.user_id.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.post_id != null) {
        oprot.writeFieldBegin(POST_ID_FIELD_DESC);
        struct.post_id.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(VISABLE_FIELD_DESC);
      oprot.writeBool(struct.visable);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserPostEdgeTupleSchemeFactory implements SchemeFactory {
    public UserPostEdgeTupleScheme getScheme() {
      return new UserPostEdgeTupleScheme();
    }
  }

  private static class UserPostEdgeTupleScheme extends TupleScheme<UserPostEdge> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserPostEdge struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.user_id.write(oprot);
      struct.post_id.write(oprot);
      oprot.writeBool(struct.visable);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserPostEdge struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.user_id = new UserID();
      struct.user_id.read(iprot);
      struct.setUser_idIsSet(true);
      struct.post_id = new PostID();
      struct.post_id.read(iprot);
      struct.setPost_idIsSet(true);
      struct.visable = iprot.readBool();
      struct.setVisableIsSet(true);
    }
  }

}

