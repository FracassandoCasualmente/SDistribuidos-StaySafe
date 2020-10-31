// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dgs.proto

package pt.tecnico.staysafe.dgs.grpc;

public interface ReportRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:pt.tecnico.staysafe.dgs.grpc.ReportRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string snifferName = 1;</code>
   * @return The snifferName.
   */
  java.lang.String getSnifferName();
  /**
   * <code>string snifferName = 1;</code>
   * @return The bytes for snifferName.
   */
  com.google.protobuf.ByteString
      getSnifferNameBytes();

  /**
   * <code>.google.protobuf.Timestamp insertionTime = 2;</code>
   * @return Whether the insertionTime field is set.
   */
  boolean hasInsertionTime();
  /**
   * <code>.google.protobuf.Timestamp insertionTime = 2;</code>
   * @return The insertionTime.
   */
  com.google.protobuf.Timestamp getInsertionTime();
  /**
   * <code>.google.protobuf.Timestamp insertionTime = 2;</code>
   */
  com.google.protobuf.TimestampOrBuilder getInsertionTimeOrBuilder();

  /**
   * <code>.pt.tecnico.staysafe.dgs.grpc.PersonType type = 3;</code>
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   * <code>.pt.tecnico.staysafe.dgs.grpc.PersonType type = 3;</code>
   * @return The type.
   */
  pt.tecnico.staysafe.dgs.grpc.PersonType getType();

  /**
   * <code>uint64 citizenId = 4;</code>
   * @return The citizenId.
   */
  long getCitizenId();

  /**
   * <code>.google.protobuf.Timestamp enterTime = 5;</code>
   * @return Whether the enterTime field is set.
   */
  boolean hasEnterTime();
  /**
   * <code>.google.protobuf.Timestamp enterTime = 5;</code>
   * @return The enterTime.
   */
  com.google.protobuf.Timestamp getEnterTime();
  /**
   * <code>.google.protobuf.Timestamp enterTime = 5;</code>
   */
  com.google.protobuf.TimestampOrBuilder getEnterTimeOrBuilder();

  /**
   * <code>.google.protobuf.Timestamp leaveTime = 6;</code>
   * @return Whether the leaveTime field is set.
   */
  boolean hasLeaveTime();
  /**
   * <code>.google.protobuf.Timestamp leaveTime = 6;</code>
   * @return The leaveTime.
   */
  com.google.protobuf.Timestamp getLeaveTime();
  /**
   * <code>.google.protobuf.Timestamp leaveTime = 6;</code>
   */
  com.google.protobuf.TimestampOrBuilder getLeaveTimeOrBuilder();
}
