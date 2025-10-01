/*
 * SteVe - SteckdosenVerwaltung - https://github.com/steve-community/steve
 * Copyright (C) 2013-2025 SteVe Community Team
 * All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package de.rwth.idsg.steve.ocpp.ws.ocpp16;

import de.rwth.idsg.ocpp.jaxb.RequestType;
import de.rwth.idsg.ocpp.jaxb.ResponseType;
import de.rwth.idsg.steve.config.DelegatingTaskScheduler;
import de.rwth.idsg.steve.config.SteveProperties;
import de.rwth.idsg.steve.ocpp.OcppProtocol;
import de.rwth.idsg.steve.ocpp.OcppVersion;
import de.rwth.idsg.steve.ocpp.soap.CentralSystemService16_SoapServer;
import de.rwth.idsg.steve.ocpp.ws.AbstractWebSocketEndpoint;
import de.rwth.idsg.steve.ocpp.ws.FutureResponseContextStore;
import de.rwth.idsg.steve.repository.OcppServerRepository;
import de.rwth.idsg.steve.ocpp.ws.data.security.*;
import ocpp.cs._2015._10.AuthorizeRequest;
import ocpp.cs._2015._10.BootNotificationRequest;
import ocpp.cs._2015._10.DataTransferRequest;
import ocpp.cs._2015._10.DiagnosticsStatusNotificationRequest;
import ocpp.cs._2015._10.FirmwareStatusNotificationRequest;
import ocpp.cs._2015._10.HeartbeatRequest;
import ocpp.cs._2015._10.MeterValuesRequest;
import ocpp.cs._2015._10.StartTransactionRequest;
import ocpp.cs._2015._10.StatusNotificationRequest;
import ocpp.cs._2015._10.StopTransactionRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author Sevket Goekay <sevketgokay@gmail.com>
 * @since 13.03.2018
 */
@Component
public class Ocpp16WebSocketEndpoint extends AbstractWebSocketEndpoint {

    private final CentralSystemService16_SoapServer server;

    public Ocpp16WebSocketEndpoint(DelegatingTaskScheduler asyncTaskScheduler,
                                   OcppServerRepository ocppServerRepository,
                                   FutureResponseContextStore futureResponseContextStore,
                                   ApplicationEventPublisher applicationEventPublisher,
                                   CentralSystemService16_SoapServer server,
                                   SteveProperties steveProperties) {
        super(asyncTaskScheduler, ocppServerRepository, futureResponseContextStore, applicationEventPublisher, steveProperties, Ocpp16TypeStore.INSTANCE);
        this.server = server;
    }

    @Override
    public OcppVersion getVersion() {
        return OcppVersion.V_16;
    }

    @Override
    public ResponseType dispatch(RequestType params, String chargeBoxId) {
        ResponseType r;

        if (params instanceof BootNotificationRequest) {
            r = server.bootNotificationWithTransport((BootNotificationRequest) params, chargeBoxId, OcppProtocol.V_16_JSON);

        } else if (params instanceof FirmwareStatusNotificationRequest) {
            r = server.firmwareStatusNotification((FirmwareStatusNotificationRequest) params, chargeBoxId);

        } else if (params instanceof StatusNotificationRequest) {
            r = server.statusNotification((StatusNotificationRequest) params, chargeBoxId);

        } else if (params instanceof MeterValuesRequest) {
            r = server.meterValues((MeterValuesRequest) params, chargeBoxId);

        } else if (params instanceof DiagnosticsStatusNotificationRequest) {
            r = server.diagnosticsStatusNotification((DiagnosticsStatusNotificationRequest) params, chargeBoxId);

        } else if (params instanceof StartTransactionRequest) {
            r = server.startTransaction((StartTransactionRequest) params, chargeBoxId);

        } else if (params instanceof StopTransactionRequest) {
            r = server.stopTransaction((StopTransactionRequest) params, chargeBoxId);

        } else if (params instanceof HeartbeatRequest) {
            r = server.heartbeat((HeartbeatRequest) params, chargeBoxId);

        } else if (params instanceof AuthorizeRequest) {
            r = server.authorize((AuthorizeRequest) params, chargeBoxId);

        } else if (params instanceof DataTransferRequest) {
            r = server.dataTransfer((DataTransferRequest) params, chargeBoxId);

        } else if (params instanceof de.rwth.idsg.steve.ocpp.ws.data.security.SignCertificateRequest) {
            r = server.signCertificate((de.rwth.idsg.steve.ocpp.ws.data.security.SignCertificateRequest) params, chargeBoxId);

        } else if (params instanceof de.rwth.idsg.steve.ocpp.ws.data.security.SecurityEventNotificationRequest) {
            r = server.securityEventNotification((de.rwth.idsg.steve.ocpp.ws.data.security.SecurityEventNotificationRequest) params, chargeBoxId);

        } else if (params instanceof de.rwth.idsg.steve.ocpp.ws.data.security.SignedFirmwareStatusNotificationRequest) {
            r = server.signedFirmwareStatusNotification((de.rwth.idsg.steve.ocpp.ws.data.security.SignedFirmwareStatusNotificationRequest) params, chargeBoxId);

        } else if (params instanceof de.rwth.idsg.steve.ocpp.ws.data.security.LogStatusNotificationRequest) {
            r = server.logStatusNotification((de.rwth.idsg.steve.ocpp.ws.data.security.LogStatusNotificationRequest) params, chargeBoxId);

        } else {
            throw new IllegalArgumentException("Unexpected RequestType: " + params.getClass().getName());
        }

        return r;
    }
}
