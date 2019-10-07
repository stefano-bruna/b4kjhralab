import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './patent.reducer';
import { IPatent } from 'app/shared/model/patent.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PatentDetail extends React.Component<IPatentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { patentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="b4KjhralabApp.patent.detail.title">Patent</Translate> [<b>{patentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="uid">
                <Translate contentKey="b4KjhralabApp.patent.uid">Uid</Translate>
              </span>
            </dt>
            <dd>{patentEntity.uid}</dd>
            <dt>
              <span id="name">
                <Translate contentKey="b4KjhralabApp.patent.name">Name</Translate>
              </span>
            </dt>
            <dd>{patentEntity.name}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="b4KjhralabApp.patent.description">Description</Translate>
              </span>
            </dt>
            <dd>{patentEntity.description}</dd>
            <dt>
              <Translate contentKey="b4KjhralabApp.patent.client">Client</Translate>
            </dt>
            <dd>{patentEntity.client ? patentEntity.client.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/patent" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/patent/${patentEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ patent }: IRootState) => ({
  patentEntity: patent.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PatentDetail);
