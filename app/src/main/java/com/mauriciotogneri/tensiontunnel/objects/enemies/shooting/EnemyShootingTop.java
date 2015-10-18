package com.mauriciotogneri.tensiontunnel.objects.enemies.shooting;

import com.mauriciotogneri.tensiontunnel.engine.Game;
import com.mauriciotogneri.tensiontunnel.engine.Renderer;
import com.mauriciotogneri.tensiontunnel.objects.Background;
import com.mauriciotogneri.tensiontunnel.objects.beams.Beam;
import com.mauriciotogneri.tensiontunnel.objects.beams.BeamDown;

public class EnemyShootingTop extends EnemyShooting
{
    public EnemyShootingTop(Game game, float x, float beamFrequency, float beamSpeed)
    {
        super(game, x - (EnemyShooting.SIZE_EXTERNAL / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE_EXTERNAL, beamFrequency, beamSpeed);
    }

    @Override
    protected Beam getNewBeam(float beamSpeed)
    {
        return new BeamDown(this.sprite.getX() + (this.sprite.getWidth() / 2f), Renderer.RESOLUTION_Y - Background.getHeight() - EnemyShooting.SIZE_EXTERNAL - Beam.getHeight(), -beamSpeed);
    }
}