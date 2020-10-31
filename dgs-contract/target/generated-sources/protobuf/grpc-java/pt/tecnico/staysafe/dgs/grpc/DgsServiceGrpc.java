package pt.tecnico.staysafe.dgs.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 *SERVICE
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.28.0)",
    comments = "Source: dgs.proto")
public final class DgsServiceGrpc {

  private DgsServiceGrpc() {}

  public static final String SERVICE_NAME = "pt.tecnico.staysafe.dgs.grpc.DgsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest,
      pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> getSnifferJoinMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sniffer_join",
      requestType = pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest,
      pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> getSnifferJoinMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest, pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> getSnifferJoinMethod;
    if ((getSnifferJoinMethod = DgsServiceGrpc.getSnifferJoinMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getSnifferJoinMethod = DgsServiceGrpc.getSnifferJoinMethod) == null) {
          DgsServiceGrpc.getSnifferJoinMethod = getSnifferJoinMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest, pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sniffer_join"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("sniffer_join"))
              .build();
        }
      }
    }
    return getSnifferJoinMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest,
      pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> getSnifferInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sniffer_info",
      requestType = pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest,
      pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> getSnifferInfoMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest, pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> getSnifferInfoMethod;
    if ((getSnifferInfoMethod = DgsServiceGrpc.getSnifferInfoMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getSnifferInfoMethod = DgsServiceGrpc.getSnifferInfoMethod) == null) {
          DgsServiceGrpc.getSnifferInfoMethod = getSnifferInfoMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest, pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sniffer_info"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("sniffer_info"))
              .build();
        }
      }
    }
    return getSnifferInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ReportRequest,
      pt.tecnico.staysafe.dgs.grpc.ReportResponse> getReportMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "report",
      requestType = pt.tecnico.staysafe.dgs.grpc.ReportRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.ReportResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ReportRequest,
      pt.tecnico.staysafe.dgs.grpc.ReportResponse> getReportMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ReportRequest, pt.tecnico.staysafe.dgs.grpc.ReportResponse> getReportMethod;
    if ((getReportMethod = DgsServiceGrpc.getReportMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getReportMethod = DgsServiceGrpc.getReportMethod) == null) {
          DgsServiceGrpc.getReportMethod = getReportMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.ReportRequest, pt.tecnico.staysafe.dgs.grpc.ReportResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "report"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.ReportRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.ReportResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("report"))
              .build();
        }
      }
    }
    return getReportMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest,
      pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> getIndividualInfectionProbabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "individual_infection_probability",
      requestType = pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest,
      pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> getIndividualInfectionProbabilityMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest, pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> getIndividualInfectionProbabilityMethod;
    if ((getIndividualInfectionProbabilityMethod = DgsServiceGrpc.getIndividualInfectionProbabilityMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getIndividualInfectionProbabilityMethod = DgsServiceGrpc.getIndividualInfectionProbabilityMethod) == null) {
          DgsServiceGrpc.getIndividualInfectionProbabilityMethod = getIndividualInfectionProbabilityMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest, pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "individual_infection_probability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("individual_infection_probability"))
              .build();
        }
      }
    }
    return getIndividualInfectionProbabilityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest,
      pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> getAggregateInfectionProbabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "aggregate_infection_probability",
      requestType = pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest,
      pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> getAggregateInfectionProbabilityMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest, pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> getAggregateInfectionProbabilityMethod;
    if ((getAggregateInfectionProbabilityMethod = DgsServiceGrpc.getAggregateInfectionProbabilityMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getAggregateInfectionProbabilityMethod = DgsServiceGrpc.getAggregateInfectionProbabilityMethod) == null) {
          DgsServiceGrpc.getAggregateInfectionProbabilityMethod = getAggregateInfectionProbabilityMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest, pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "aggregate_infection_probability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("aggregate_infection_probability"))
              .build();
        }
      }
    }
    return getAggregateInfectionProbabilityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.PingRequest,
      pt.tecnico.staysafe.dgs.grpc.PingResponse> getCtrlPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ctrl_ping",
      requestType = pt.tecnico.staysafe.dgs.grpc.PingRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.PingRequest,
      pt.tecnico.staysafe.dgs.grpc.PingResponse> getCtrlPingMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.PingRequest, pt.tecnico.staysafe.dgs.grpc.PingResponse> getCtrlPingMethod;
    if ((getCtrlPingMethod = DgsServiceGrpc.getCtrlPingMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getCtrlPingMethod = DgsServiceGrpc.getCtrlPingMethod) == null) {
          DgsServiceGrpc.getCtrlPingMethod = getCtrlPingMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.PingRequest, pt.tecnico.staysafe.dgs.grpc.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ctrl_ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("ctrl_ping"))
              .build();
        }
      }
    }
    return getCtrlPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ClearRequest,
      pt.tecnico.staysafe.dgs.grpc.ClearResponse> getCtrlClearMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ctrl_clear",
      requestType = pt.tecnico.staysafe.dgs.grpc.ClearRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.ClearResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ClearRequest,
      pt.tecnico.staysafe.dgs.grpc.ClearResponse> getCtrlClearMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.ClearRequest, pt.tecnico.staysafe.dgs.grpc.ClearResponse> getCtrlClearMethod;
    if ((getCtrlClearMethod = DgsServiceGrpc.getCtrlClearMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getCtrlClearMethod = DgsServiceGrpc.getCtrlClearMethod) == null) {
          DgsServiceGrpc.getCtrlClearMethod = getCtrlClearMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.ClearRequest, pt.tecnico.staysafe.dgs.grpc.ClearResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ctrl_clear"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.ClearRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.ClearResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("ctrl_clear"))
              .build();
        }
      }
    }
    return getCtrlClearMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.InitRequest,
      pt.tecnico.staysafe.dgs.grpc.InitResponse> getCtrlInitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ctrl_init",
      requestType = pt.tecnico.staysafe.dgs.grpc.InitRequest.class,
      responseType = pt.tecnico.staysafe.dgs.grpc.InitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.InitRequest,
      pt.tecnico.staysafe.dgs.grpc.InitResponse> getCtrlInitMethod() {
    io.grpc.MethodDescriptor<pt.tecnico.staysafe.dgs.grpc.InitRequest, pt.tecnico.staysafe.dgs.grpc.InitResponse> getCtrlInitMethod;
    if ((getCtrlInitMethod = DgsServiceGrpc.getCtrlInitMethod) == null) {
      synchronized (DgsServiceGrpc.class) {
        if ((getCtrlInitMethod = DgsServiceGrpc.getCtrlInitMethod) == null) {
          DgsServiceGrpc.getCtrlInitMethod = getCtrlInitMethod =
              io.grpc.MethodDescriptor.<pt.tecnico.staysafe.dgs.grpc.InitRequest, pt.tecnico.staysafe.dgs.grpc.InitResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ctrl_init"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.InitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pt.tecnico.staysafe.dgs.grpc.InitResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DgsServiceMethodDescriptorSupplier("ctrl_init"))
              .build();
        }
      }
    }
    return getCtrlInitMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DgsServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DgsServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DgsServiceStub>() {
        @java.lang.Override
        public DgsServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DgsServiceStub(channel, callOptions);
        }
      };
    return DgsServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DgsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DgsServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DgsServiceBlockingStub>() {
        @java.lang.Override
        public DgsServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DgsServiceBlockingStub(channel, callOptions);
        }
      };
    return DgsServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DgsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DgsServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DgsServiceFutureStub>() {
        @java.lang.Override
        public DgsServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DgsServiceFutureStub(channel, callOptions);
        }
      };
    return DgsServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   *SERVICE
   * </pre>
   */
  public static abstract class DgsServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void snifferJoin(pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSnifferJoinMethod(), responseObserver);
    }

    /**
     */
    public void snifferInfo(pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSnifferInfoMethod(), responseObserver);
    }

    /**
     */
    public void report(pt.tecnico.staysafe.dgs.grpc.ReportRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ReportResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getReportMethod(), responseObserver);
    }

    /**
     */
    public void individualInfectionProbability(pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getIndividualInfectionProbabilityMethod(), responseObserver);
    }

    /**
     */
    public void aggregateInfectionProbability(pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAggregateInfectionProbabilityMethod(), responseObserver);
    }

    /**
     */
    public void ctrlPing(pt.tecnico.staysafe.dgs.grpc.PingRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.PingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCtrlPingMethod(), responseObserver);
    }

    /**
     */
    public void ctrlClear(pt.tecnico.staysafe.dgs.grpc.ClearRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ClearResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCtrlClearMethod(), responseObserver);
    }

    /**
     */
    public void ctrlInit(pt.tecnico.staysafe.dgs.grpc.InitRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.InitResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCtrlInitMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSnifferJoinMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest,
                pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse>(
                  this, METHODID_SNIFFER_JOIN)))
          .addMethod(
            getSnifferInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest,
                pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse>(
                  this, METHODID_SNIFFER_INFO)))
          .addMethod(
            getReportMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.ReportRequest,
                pt.tecnico.staysafe.dgs.grpc.ReportResponse>(
                  this, METHODID_REPORT)))
          .addMethod(
            getIndividualInfectionProbabilityMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest,
                pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse>(
                  this, METHODID_INDIVIDUAL_INFECTION_PROBABILITY)))
          .addMethod(
            getAggregateInfectionProbabilityMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest,
                pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse>(
                  this, METHODID_AGGREGATE_INFECTION_PROBABILITY)))
          .addMethod(
            getCtrlPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.PingRequest,
                pt.tecnico.staysafe.dgs.grpc.PingResponse>(
                  this, METHODID_CTRL_PING)))
          .addMethod(
            getCtrlClearMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.ClearRequest,
                pt.tecnico.staysafe.dgs.grpc.ClearResponse>(
                  this, METHODID_CTRL_CLEAR)))
          .addMethod(
            getCtrlInitMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                pt.tecnico.staysafe.dgs.grpc.InitRequest,
                pt.tecnico.staysafe.dgs.grpc.InitResponse>(
                  this, METHODID_CTRL_INIT)))
          .build();
    }
  }

  /**
   * <pre>
   *SERVICE
   * </pre>
   */
  public static final class DgsServiceStub extends io.grpc.stub.AbstractAsyncStub<DgsServiceStub> {
    private DgsServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DgsServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DgsServiceStub(channel, callOptions);
    }

    /**
     */
    public void snifferJoin(pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSnifferJoinMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void snifferInfo(pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSnifferInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void report(pt.tecnico.staysafe.dgs.grpc.ReportRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ReportResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getReportMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void individualInfectionProbability(pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getIndividualInfectionProbabilityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void aggregateInfectionProbability(pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAggregateInfectionProbabilityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ctrlPing(pt.tecnico.staysafe.dgs.grpc.PingRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.PingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCtrlPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ctrlClear(pt.tecnico.staysafe.dgs.grpc.ClearRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ClearResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCtrlClearMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ctrlInit(pt.tecnico.staysafe.dgs.grpc.InitRequest request,
        io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.InitResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCtrlInitMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   *SERVICE
   * </pre>
   */
  public static final class DgsServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<DgsServiceBlockingStub> {
    private DgsServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DgsServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DgsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse snifferJoin(pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest request) {
      return blockingUnaryCall(
          getChannel(), getSnifferJoinMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse snifferInfo(pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getSnifferInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.ReportResponse report(pt.tecnico.staysafe.dgs.grpc.ReportRequest request) {
      return blockingUnaryCall(
          getChannel(), getReportMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse individualInfectionProbability(pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest request) {
      return blockingUnaryCall(
          getChannel(), getIndividualInfectionProbabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse aggregateInfectionProbability(pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest request) {
      return blockingUnaryCall(
          getChannel(), getAggregateInfectionProbabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.PingResponse ctrlPing(pt.tecnico.staysafe.dgs.grpc.PingRequest request) {
      return blockingUnaryCall(
          getChannel(), getCtrlPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.ClearResponse ctrlClear(pt.tecnico.staysafe.dgs.grpc.ClearRequest request) {
      return blockingUnaryCall(
          getChannel(), getCtrlClearMethod(), getCallOptions(), request);
    }

    /**
     */
    public pt.tecnico.staysafe.dgs.grpc.InitResponse ctrlInit(pt.tecnico.staysafe.dgs.grpc.InitRequest request) {
      return blockingUnaryCall(
          getChannel(), getCtrlInitMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   *SERVICE
   * </pre>
   */
  public static final class DgsServiceFutureStub extends io.grpc.stub.AbstractFutureStub<DgsServiceFutureStub> {
    private DgsServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DgsServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DgsServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse> snifferJoin(
        pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSnifferJoinMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse> snifferInfo(
        pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSnifferInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.ReportResponse> report(
        pt.tecnico.staysafe.dgs.grpc.ReportRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getReportMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse> individualInfectionProbability(
        pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getIndividualInfectionProbabilityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse> aggregateInfectionProbability(
        pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAggregateInfectionProbabilityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.PingResponse> ctrlPing(
        pt.tecnico.staysafe.dgs.grpc.PingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCtrlPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.ClearResponse> ctrlClear(
        pt.tecnico.staysafe.dgs.grpc.ClearRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCtrlClearMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pt.tecnico.staysafe.dgs.grpc.InitResponse> ctrlInit(
        pt.tecnico.staysafe.dgs.grpc.InitRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCtrlInitMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SNIFFER_JOIN = 0;
  private static final int METHODID_SNIFFER_INFO = 1;
  private static final int METHODID_REPORT = 2;
  private static final int METHODID_INDIVIDUAL_INFECTION_PROBABILITY = 3;
  private static final int METHODID_AGGREGATE_INFECTION_PROBABILITY = 4;
  private static final int METHODID_CTRL_PING = 5;
  private static final int METHODID_CTRL_CLEAR = 6;
  private static final int METHODID_CTRL_INIT = 7;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DgsServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DgsServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SNIFFER_JOIN:
          serviceImpl.snifferJoin((pt.tecnico.staysafe.dgs.grpc.SnifferJoinRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferJoinResponse>) responseObserver);
          break;
        case METHODID_SNIFFER_INFO:
          serviceImpl.snifferInfo((pt.tecnico.staysafe.dgs.grpc.SnifferInfoRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.SnifferInfoResponse>) responseObserver);
          break;
        case METHODID_REPORT:
          serviceImpl.report((pt.tecnico.staysafe.dgs.grpc.ReportRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ReportResponse>) responseObserver);
          break;
        case METHODID_INDIVIDUAL_INFECTION_PROBABILITY:
          serviceImpl.individualInfectionProbability((pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.IndividualInfectionProbabilityResponse>) responseObserver);
          break;
        case METHODID_AGGREGATE_INFECTION_PROBABILITY:
          serviceImpl.aggregateInfectionProbability((pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.AggregateInfectionProbabilityResponse>) responseObserver);
          break;
        case METHODID_CTRL_PING:
          serviceImpl.ctrlPing((pt.tecnico.staysafe.dgs.grpc.PingRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.PingResponse>) responseObserver);
          break;
        case METHODID_CTRL_CLEAR:
          serviceImpl.ctrlClear((pt.tecnico.staysafe.dgs.grpc.ClearRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.ClearResponse>) responseObserver);
          break;
        case METHODID_CTRL_INIT:
          serviceImpl.ctrlInit((pt.tecnico.staysafe.dgs.grpc.InitRequest) request,
              (io.grpc.stub.StreamObserver<pt.tecnico.staysafe.dgs.grpc.InitResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class DgsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DgsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return pt.tecnico.staysafe.dgs.grpc.Dgs.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DgsService");
    }
  }

  private static final class DgsServiceFileDescriptorSupplier
      extends DgsServiceBaseDescriptorSupplier {
    DgsServiceFileDescriptorSupplier() {}
  }

  private static final class DgsServiceMethodDescriptorSupplier
      extends DgsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    DgsServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DgsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DgsServiceFileDescriptorSupplier())
              .addMethod(getSnifferJoinMethod())
              .addMethod(getSnifferInfoMethod())
              .addMethod(getReportMethod())
              .addMethod(getIndividualInfectionProbabilityMethod())
              .addMethod(getAggregateInfectionProbabilityMethod())
              .addMethod(getCtrlPingMethod())
              .addMethod(getCtrlClearMethod())
              .addMethod(getCtrlInitMethod())
              .build();
        }
      }
    }
    return result;
  }
}
