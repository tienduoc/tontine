package com.tontine.app.service;

import java.util.List;
import java.util.Optional;

import com.tontine.app.domain.HuiVien;
import com.tontine.app.repository.HuiVienRepository;
import com.tontine.app.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class HuiVienService {

    private static final Logger log = LoggerFactory.getLogger( HuiVienService.class );
    private static final String SERVICE_NAME = "huiVien";

    private final HuiVienRepository huiVienRepository;

    public HuiVienService( HuiVienRepository huiVienRepository ) {
        this.huiVienRepository = huiVienRepository;
    }

    public HuiVien save( HuiVien huiVien ) {
        log.debug( "Request to save HuiVien : {}", huiVien );
        huiVienRepository.findHuiViensByHoTenWithChiTietHuis( huiVien.getHoTen() )
            .ifPresent( existing -> {
                throw new BadRequestAlertException( "Hui vien đã tồn tại", SERVICE_NAME, "name_exists" );
            } );
        return huiVienRepository.save( huiVien );
    }

    public HuiVien update( HuiVien huiVien ) {
        log.debug( "Request to update HuiVien : {}", huiVien );
        return huiVienRepository.save( huiVien );
    }

    public Optional<HuiVien> partialUpdate( HuiVien huiVien ) {
        log.debug( "Request to partially update HuiVien : {}", huiVien );

        return huiVienRepository.findByIdWithChiTietHuis( huiVien.getId() )
            .map( existing -> {
                if ( huiVien.getHoTen() != null ) {
                    existing.setHoTen( huiVien.getHoTen() );
                }
                if ( huiVien.getSdt() != null ) {
                    existing.setSdt( huiVien.getSdt() );
                }
                return huiVienRepository.save( existing );
            } );
    }

    @Transactional( readOnly = true )
    public Page<HuiVien> findAll( Pageable pageable ) {
        log.debug( "Request to get all HuiVien" );
        return huiVienRepository.findAll( pageable );
    }

    @Transactional( readOnly = true )
    public List<HuiVien> findAllWithChiTietHuis() {
        log.debug( "Request to get all HuiVien with ChiTietHuis" );
        return huiVienRepository.findAllWithChiTietHuis();
    }

    @Transactional( readOnly = true )
    public Optional<HuiVien> findOne( Long id ) {
        log.debug( "Request to get HuiVien : {}", id );
        return huiVienRepository.findByIdWithChiTietHuis( id );
    }

    public void delete( Long id ) {
        log.debug( "Request to delete HuiVien : {}", id );
        huiVienRepository.deleteById( id );
    }
}
