package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.model.VerificationToken;
import com.equipo2.Integrador.repository.VerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final ClienteDTOModelMapper clienteDTOModelMapper;
    private final Logger logger = Logger.getLogger(VerificationTokenService.class);


    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository, ClienteDTOModelMapper clienteDTOModelMapper) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.clienteDTOModelMapper = clienteDTOModelMapper;
    }


    public VerificationToken getVerificationToken(String VerificationToken)
    {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createVerificationToken(ClienteDTO clienteDTO, String token)
    {
        VerificationToken myToken = new VerificationToken(token, clienteDTOModelMapper.toModel(clienteDTO));
        verificationTokenRepository.save(myToken);
    }
}